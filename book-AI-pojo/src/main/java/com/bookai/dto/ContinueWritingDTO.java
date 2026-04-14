package com.bookai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContinueWritingDTO {
    @NotNull(message = "书籍ID不能为空")
    private Long bookId;

    private String prompt; // 自定义续写提示词，可选

    private Integer maxTokens = 2000; // 最大生成token数，默认2000

    private Double temperature = 0.7; // 创意度，默认0.7
}
