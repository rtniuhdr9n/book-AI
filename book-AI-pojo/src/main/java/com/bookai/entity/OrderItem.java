package com.bookai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;
    private Long bookId;
    private String bookTitle;
    private String bookCover;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
