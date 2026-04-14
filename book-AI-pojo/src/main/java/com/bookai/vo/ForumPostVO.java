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
public class ForumPostVO {
    private Long id;
    private Long sectionId;
    private String sectionName;
    private Long userId;
    private String username;
    private String avatar;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer replyCount;
    private Integer likeCount;
    private Integer isTop;
    private Integer isEssence;
    private LocalDateTime createTime;
}
