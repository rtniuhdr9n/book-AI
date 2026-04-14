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
public class BookVO {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Long categoryId;
    private String categoryName;
    private String cover;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private Integer isOnSale;
    private LocalDateTime publishDate;
    private LocalDateTime createTime;
}
