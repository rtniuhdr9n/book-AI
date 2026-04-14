package com.bookai.controller.user;

import com.bookai.common.Result;
import com.bookai.dto.CartItemDTO;
import com.bookai.service.CartService;
import com.bookai.vo.CartItemVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    public Result list(@RequestAttribute("userId") Long userId) {
        List<CartItemVO> list = cartService.getCartItems(userId);
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result add(@RequestAttribute("userId") Long userId, @Valid @RequestBody CartItemDTO dto) {
        boolean success = cartService.addToCart(userId, dto);
        if (success) {
            return Result.success();
        }
        return Result.error("添加失败");
    }

    @PutMapping("/update/{cartItemId}")
    public Result update(@RequestAttribute("userId") Long userId, 
                         @PathVariable Long cartItemId,
                         @RequestParam Integer quantity) {
        boolean success = cartService.updateCartItemQuantity(userId, cartItemId, quantity);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/remove")
    public Result remove(@RequestAttribute("userId") Long userId, 
                         @RequestParam List<Long> cartItemIds) {
        boolean success = cartService.removeCartItems(userId, cartItemIds);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @DeleteMapping("/clear")
    public Result clear(@RequestAttribute("userId") Long userId) {
        boolean success = cartService.clearCart(userId);
        if (success) {
            return Result.success();
        }
        return Result.error("清空失败");
    }
}
