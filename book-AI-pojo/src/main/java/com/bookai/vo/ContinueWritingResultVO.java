package com.bookai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContinueWritingResultVO {
    private Long chapterId; // 新生成的章节ID
    private String title; // 章节标题
    private String content; // 生成的内容
    private Integer chapterNum; // 章节序号
    private Boolean success; // 是否成功
    private String message; // 提示信息
}
