package com.bookai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {

    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    private String remark;

    private List<Long> cartItemIds;
}
