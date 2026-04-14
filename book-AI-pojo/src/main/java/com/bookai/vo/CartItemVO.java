package com.bookai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemVO {
    private Long id;
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private String bookCover;
    private BigDecimal bookPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createTime;
}
