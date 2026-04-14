package com.bookai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumSectionVO {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Integer sort;
    private Integer postCount;
    private LocalDateTime createTime;
}
