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
@TableName("forum_post")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumPost {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long sectionId;
    private Long userId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer replyCount;
    private Integer likeCount;
    private Integer isTop;
    private Integer isEssence;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
