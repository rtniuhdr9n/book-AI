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
@TableName("book_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String author;
    private String publisher;
    private Long categoryId;
    private String cover;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private Integer isOnSale;
    private LocalDateTime publishDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
