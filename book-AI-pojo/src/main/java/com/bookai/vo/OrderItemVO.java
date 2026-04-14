package com.bookai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookCover;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
