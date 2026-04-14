package com.bookai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResultVO<T> {
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPages;
    private List<T> list;

    public static <T> PageResultVO<T> of(Long total, Integer pageNum, Integer pageSize, List<T> list) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        return PageResultVO.<T>builder()
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .list(list)
                .build();
    }
}
