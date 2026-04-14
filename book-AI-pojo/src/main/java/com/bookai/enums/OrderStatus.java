package com.bookai.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING_PAYMENT(0, "待付款"),
    PAID(1, "已付款"),
    SHIPPED(2, "已发货"),
    DELIVERED(3, "已送达"),
    COMPLETED(4, "已完成"),
    CANCELLED(5, "已取消"),
    REFUNDED(6, "已退款");

    private final Integer code;
    private final String desc;

    OrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderStatus getByCode(Integer code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
