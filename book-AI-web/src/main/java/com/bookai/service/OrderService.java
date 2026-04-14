package com.bookai.service;

import com.bookai.dto.OrderCreateDTO;
import com.bookai.dto.OrderPayDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.vo.OrderVO;
import com.bookai.vo.PageResultVO;

public interface OrderService {

    OrderVO createOrder(Long userId, OrderCreateDTO dto);

    boolean payOrder(Long userId, OrderPayDTO dto);

    OrderVO getOrderDetail(Long userId, Long orderId);

    PageResultVO<OrderVO> getUserOrders(Long userId, PageQueryDTO dto);

    boolean cancelOrder(Long userId, Long orderId);

    boolean confirmReceive(Long userId, Long orderId);

    // Admin
    PageResultVO<OrderVO> getAllOrders(PageQueryDTO dto);

    boolean updateOrderStatus(Long orderId, Integer status);

    boolean shipOrder(Long orderId, String trackingNo);
}
