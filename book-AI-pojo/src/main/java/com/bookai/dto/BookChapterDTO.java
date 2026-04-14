package com.bookai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookChapterDTO {
    private Long id;

    @NotNull(message = "书籍ID不能为空")
    private Long bookId;

    @NotBlank(message = "章节标题不能为空")
    private String title;

    @NotNull(message = "章节序号不能为空")
    private Integer chapterNum;

    private String content;

    @NotNull(message = "是否免费不能为空")
    private Integer isFree;
}
