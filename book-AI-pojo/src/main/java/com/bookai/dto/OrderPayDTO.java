package com.bookai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderPayDTO {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotBlank(message = "支付方式不能为空")
    private String payMethod;
}
