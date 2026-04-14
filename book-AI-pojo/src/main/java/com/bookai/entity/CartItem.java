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
@TableName("cart_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long bookId;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
