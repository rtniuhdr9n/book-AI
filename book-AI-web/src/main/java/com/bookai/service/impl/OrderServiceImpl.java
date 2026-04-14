package com.bookai.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookai.dto.OrderCreateDTO;
import com.bookai.dto.OrderPayDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.entity.*;
import com.bookai.enums.OrderStatus;
import com.bookai.mapper.*;
import com.bookai.service.OrderService;
import com.bookai.vo.OrderItemVO;
import com.bookai.vo.OrderVO;
import com.bookai.vo.PageResultVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public OrderVO createOrder(Long userId, OrderCreateDTO dto) {
        // 获取收货地址
        UserAddress address = userAddressMapper.selectById(dto.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("收货地址不存在");
        }

        // 获取购物车商品
        List<CartItem> cartItems;
        if (dto.getCartItemIds() != null && !dto.getCartItemIds().isEmpty()) {
            cartItems = cartItemMapper.selectBatchIds(dto.getCartItemIds())
                    .stream()
                    .filter(item -> item.getUserId().equals(userId))
                    .collect(Collectors.toList());
        } else {
            cartItems = cartItemMapper.selectByUserId(userId);
        }

        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        // 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BookInfo book = bookInfoMapper.selectById(item.getBookId());
            if (book == null || book.getIsOnSale() != 1) {
                throw new RuntimeException("商品已下架或不存在");
            }
            if (book.getStock() < item.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + book.getTitle());
            }
            totalAmount = totalAmount.add(book.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        // 创建订单
        String orderNo = generateOrderNo();
        OrderInfo order = OrderInfo.builder()
                .orderNo(orderNo)
                .userId(userId)
                .addressId(dto.getAddressId())
                .totalAmount(totalAmount)
                .discountAmount(BigDecimal.ZERO)
                .payAmount(totalAmount)
                .status(OrderStatus.PENDING_PAYMENT.getCode())
                .remark(dto.getRemark())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        orderInfoMapper.insert(order);

        // 创建订单明细
        for (CartItem item : cartItems) {
            BookInfo book = bookInfoMapper.selectById(item.getBookId());
            OrderItem orderItem = OrderItem.builder()
                    .orderId(order.getId())
                    .bookId(item.getBookId())
                    .bookTitle(book.getTitle())
                    .bookCover(book.getCover())
                    .quantity(item.getQuantity())
                    .unitPrice(book.getPrice())
                    .totalPrice(book.getPrice().multiply(new BigDecimal(item.getQuantity())))
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            orderItemMapper.insert(orderItem);

            // 扣减库存
            bookInfoMapper.decreaseStock(item.getBookId(), item.getQuantity());
        }

        // 清空购物车
        cartItemMapper.deleteByUserIdAndIds(userId, cartItems.stream().map(CartItem::getId).collect(Collectors.toList()));

        // 发送订单创建消息到RabbitMQ（异步处理）
        sendOrderCreateMessage(order);

        return getOrderDetail(userId, order.getId());
    }

    @Override
    @Transactional
    public boolean payOrder(Long userId, OrderPayDTO dto) {
        OrderInfo order = orderInfoMapper.selectById(dto.getOrderId());
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }
        if (!OrderStatus.PENDING_PAYMENT.getCode().equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus(OrderStatus.PAID.getCode());
        order.setPayTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 增加销量
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        for (OrderItem item : items) {
            bookInfoMapper.increaseSales(item.getBookId(), item.getQuantity());
        }

        return orderInfoMapper.updateById(order) > 0;
    }

    @Override
    public OrderVO getOrderDetail(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return null;
        }
        return convertToVO(order);
    }

    @Override
    public PageResultVO<OrderVO> getUserOrders(Long userId, PageQueryDTO dto) {
        Page<OrderInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<OrderInfo> resultPage = orderInfoMapper.selectPageByUserId(page, userId);

        List<OrderVO> voList = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResultVO.of(resultPage.getTotal(), dto.getPageNum(), dto.getPageSize(), voList);
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }
        if (!OrderStatus.PENDING_PAYMENT.getCode().equals(order.getStatus())) {
            throw new RuntimeException("只能取消待付款订单");
        }

        order.setStatus(OrderStatus.CANCELLED.getCode());
        order.setUpdateTime(LocalDateTime.now());

        // 恢复库存
        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        for (OrderItem item : items) {
            BookInfo book = bookInfoMapper.selectById(item.getBookId());
            if (book != null) {
                book.setStock(book.getStock() + item.getQuantity());
                bookInfoMapper.updateById(book);
            }
        }

        boolean success = orderInfoMapper.updateById(order) > 0;
        
        // 发送订单取消消息到RabbitMQ（异步处理）
        if (success) {
            sendOrderCancelMessage(order);
        }
        
        return success;
    }

    @Override
    public boolean confirmReceive(Long userId, Long orderId) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }
        if (!OrderStatus.SHIPPED.getCode().equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus(OrderStatus.COMPLETED.getCode());
        order.setReceiveTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return orderInfoMapper.updateById(order) > 0;
    }

    @Override
    public PageResultVO<OrderVO> getAllOrders(PageQueryDTO dto) {
        Page<OrderInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<OrderInfo> resultPage = orderInfoMapper.selectPage(page, null);

        List<OrderVO> voList = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResultVO.of(resultPage.getTotal(), dto.getPageNum(), dto.getPageSize(), voList);
    }

    @Override
    public boolean updateOrderStatus(Long orderId, Integer status) {
        OrderInfo order = new OrderInfo();
        order.setId(orderId);
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        return orderInfoMapper.updateById(order) > 0;
    }

    @Override
    public boolean shipOrder(Long orderId, String trackingNo) {
        OrderInfo order = orderInfoMapper.selectById(orderId);
        if (order == null) {
            return false;
        }
        if (!OrderStatus.PAID.getCode().equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus(OrderStatus.SHIPPED.getCode());
        order.setShipTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return orderInfoMapper.updateById(order) > 0;
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return timestamp + random;
    }

    /**
     * 发送订单创建消息
     */
    private void sendOrderCreateMessage(OrderInfo order) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("orderId", order.getId());
            message.put("orderNo", order.getOrderNo());
            message.put("userId", order.getUserId());
            message.put("payAmount", order.getPayAmount());
            message.put("createTime", order.getCreateTime().toString());
            
            rabbitTemplate.convertAndSend("order.create", message);
        } catch (Exception e) {
            // 消息发送失败不影响主流程，记录日志即可
            e.printStackTrace();
        }
    }

    /**
     * 发送订单取消消息
     */
    private void sendOrderCancelMessage(OrderInfo order) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("orderId", order.getId());
            message.put("orderNo", order.getOrderNo());
            message.put("userId", order.getUserId());
            message.put("cancelTime", LocalDateTime.now().toString());
            
            rabbitTemplate.convertAndSend("order.cancel", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OrderVO convertToVO(OrderInfo order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);

        OrderStatus status = OrderStatus.getByCode(order.getStatus());
        if (status != null) {
            vo.setStatusDesc(status.getDesc());
        }

        // 设置收货地址信息
        UserAddress address = userAddressMapper.selectById(order.getAddressId());
        if (address != null) {
            vo.setReceiverName(address.getReceiverName());
            vo.setReceiverPhone(address.getReceiverPhone());
            vo.setFullAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress());
        }

        // 设置订单明细
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemVO> itemVOs = items.stream().map(item -> {
            OrderItemVO itemVO = new OrderItemVO();
            BeanUtils.copyProperties(item, itemVO);
            return itemVO;
        }).collect(Collectors.toList());
        vo.setItems(itemVOs);

        return vo;
    }
}
