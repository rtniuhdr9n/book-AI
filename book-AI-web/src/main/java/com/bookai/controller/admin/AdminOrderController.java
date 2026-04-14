package com.bookai.controller.admin;

import com.bookai.common.Result;
import com.bookai.dto.PageQueryDTO;
import com.bookai.service.OrderService;
import com.bookai.vo.OrderVO;
import com.bookai.vo.PageResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result list(PageQueryDTO dto) {
        PageResultVO<OrderVO> result = orderService.getAllOrders(dto);
        return Result.success(result);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {
        // 使用 getOrderDetail 需要 userId，这里简化处理
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = orderService.updateOrderStatus(id, status);
        if (success) {
            return Result.success();
        }
        return Result.error("更新状态失败");
    }

    @PutMapping("/ship/{id}")
    public Result ship(@PathVariable Long id, @RequestParam(required = false) String trackingNo) {
        boolean success = orderService.shipOrder(id, trackingNo);
        if (success) {
            return Result.success();
        }
        return Result.error("发货失败");
    }
}
