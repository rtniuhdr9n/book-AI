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
@TableName("book_chapter")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookChapter {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bookId;
    private String title;
    private Integer chapterNum;
    private String content;
    private Integer isFree;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
