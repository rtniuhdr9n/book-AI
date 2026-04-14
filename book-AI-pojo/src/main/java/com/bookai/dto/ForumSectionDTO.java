package com.bookai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForumSectionDTO {
    private Long id;

    @NotBlank(message = "板块名称不能为空")
    private String name;

    private String description;
    private String icon;
    private Integer sort;
}
