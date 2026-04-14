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
public class BookChapterVO {
    private Long id;
    private Long bookId;
    private String title;
    private Integer chapterNum;
    private String content;
    private Integer isFree;
    private LocalDateTime createTime;
}
