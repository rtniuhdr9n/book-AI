package com.bookai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class
BookCategoryDTO {
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private Long parentId;

    @NotNull(message = "层级不能为空")
    private Integer level;

    private Integer sort;
    private String icon;
}
