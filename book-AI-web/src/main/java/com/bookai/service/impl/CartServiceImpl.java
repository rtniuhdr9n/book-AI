package com.bookai.service.impl;

import com.bookai.dto.CartItemDTO;
import com.bookai.entity.BookInfo;
import com.bookai.entity.CartItem;
import com.bookai.mapper.BookInfoMapper;
import com.bookai.mapper.CartItemMapper;
import com.bookai.service.CartService;
import com.bookai.vo.CartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Override
    public List<CartItemVO> getCartItems(Long userId) {
        List<CartItem> items = cartItemMapper.selectByUserId(userId);
        return items.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public boolean addToCart(Long userId, CartItemDTO dto) {
        // 检查书籍是否存在
        BookInfo book = bookInfoMapper.selectById(dto.getBookId());
        if (book == null) {
            throw new RuntimeException("书籍不存在");
        }

        // 检查购物车中是否已有该商品
        CartItem existing = cartItemMapper.selectByUserIdAndBookId(userId, dto.getBookId());
        if (existing != null) {
            // 更新数量
            existing.setQuantity(existing.getQuantity() + dto.getQuantity());
            existing.setPrice(book.getPrice());
            existing.setUpdateTime(LocalDateTime.now());
            return cartItemMapper.updateById(existing) > 0;
        } else {
            // 新增
            CartItem item = CartItem.builder()
                    .userId(userId)
                    .bookId(dto.getBookId())
                    .quantity(dto.getQuantity())
                    .price(book.getPrice())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            return cartItemMapper.insert(item) > 0;
        }
    }

    @Override
    public boolean updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem item = cartItemMapper.selectById(cartItemId);
        if (item == null || !item.getUserId().equals(userId)) {
            return false;
        }
        item.setQuantity(quantity);
        item.setUpdateTime(LocalDateTime.now());
        return cartItemMapper.updateById(item) > 0;
    }

    @Override
    public boolean removeCartItems(Long userId, List<Long> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return true;
        }
        return cartItemMapper.deleteByUserIdAndIds(userId, cartItemIds) > 0;
    }

    @Override
    public boolean clearCart(Long userId) {
        return cartItemMapper.clearByUserId(userId) > 0;
    }

    private CartItemVO convertToVO(CartItem item) {
        CartItemVO vo = new CartItemVO();
        vo.setId(item.getId());
        vo.setUserId(item.getUserId());
        vo.setBookId(item.getBookId());
        vo.setQuantity(item.getQuantity());
        vo.setCreateTime(item.getCreateTime());

        // 获取书籍信息
        BookInfo book = bookInfoMapper.selectById(item.getBookId());
        if (book != null) {
            vo.setBookTitle(book.getTitle());
            vo.setBookCover(book.getCover());
            vo.setBookPrice(book.getPrice());
            vo.setTotalPrice(book.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        return vo;
    }
}
