package com.bookai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("forum_section")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumSection {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String icon;
    private Integer sort;
    private Integer postCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
