package com.bookai.service;

import com.bookai.dto.CartItemDTO;
import com.bookai.vo.CartItemVO;

import java.util.List;

public interface CartService {

    List<CartItemVO> getCartItems(Long userId);

    boolean addToCart(Long userId, CartItemDTO dto);

    boolean updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity);

    boolean removeCartItems(Long userId, List<Long> cartItemIds);

    boolean clearCart(Long userId);
}
