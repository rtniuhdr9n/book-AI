package com.bookai.controller.user;

import com.bookai.common.Result;
import com.bookai.dto.OrderCreateDTO;
import com.bookai.dto.OrderPayDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.service.OrderService;
import com.bookai.vo.OrderVO;
import com.bookai.vo.PageResultVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result list(@RequestAttribute("userId") Long userId, PageQueryDTO dto) {
        PageResultVO<OrderVO> result = orderService.getUserOrders(userId, dto);
        return Result.success(result);
    }

    @GetMapping("/detail/{orderId}")
    public Result detail(@RequestAttribute("userId") Long userId, @PathVariable Long orderId) {
        OrderVO vo = orderService.getOrderDetail(userId, orderId);
        if (vo == null) {
            return Result.error("订单不存在");
        }
        return Result.success(vo);
    }

    @PostMapping("/create")
    public Result create(@RequestAttribute("userId") Long userId, @Valid @RequestBody OrderCreateDTO dto) {
        OrderVO vo = orderService.createOrder(userId, dto);
        return Result.success(vo);
    }

    @PostMapping("/pay")
    public Result pay(@RequestAttribute("userId") Long userId, @Valid @RequestBody OrderPayDTO dto) {
        boolean success = orderService.payOrder(userId, dto);
        if (success) {
            return Result.success();
        }
        return Result.error("支付失败");
    }

    @PutMapping("/cancel/{orderId}")
    public Result cancel(@RequestAttribute("userId") Long userId, @PathVariable Long orderId) {
        boolean success = orderService.cancelOrder(userId, orderId);
        if (success) {
            return Result.success();
        }
        return Result.error("取消失败");
    }

    @PutMapping("/confirm/{orderId}")
    public Result confirm(@RequestAttribute("userId") Long userId, @PathVariable Long orderId) {
        boolean success = orderService.confirmReceive(userId, orderId);
        if (success) {
            return Result.success();
        }
        return Result.error("确认收货失败");
    }
}
