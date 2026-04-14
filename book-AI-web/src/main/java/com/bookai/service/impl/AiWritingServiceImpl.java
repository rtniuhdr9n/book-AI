package com.bookai.service.impl;

import com.bookai.config.LLMProperties;
import com.bookai.dto.ContinueWritingDTO;
import com.bookai.entity.BookChapter;
import com.bookai.entity.BookInfo;
import com.bookai.mapper.BookChapterMapper;
import com.bookai.mapper.BookInfoMapper;
import com.bookai.service.AiWritingService;
import com.bookai.vo.ContinueWritingResultVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AiWritingServiceImpl implements AiWritingService {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private BookChapterMapper bookChapterMapper;

    @Autowired
    private LLMProperties llmProperties;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public ContinueWritingResultVO continueWriting(ContinueWritingDTO dto) {
        try {
            // 1. 验证书籍是否存在
            BookInfo book = bookInfoMapper.selectById(dto.getBookId());
            if (book == null) {
                return ContinueWritingResultVO.builder()
                        .success(false)
                        .message("书籍不存在")
                        .build();
            }

            // 2. 获取该书的所有章节
            List<BookChapter> chapters = bookChapterMapper.selectByBookId(dto.getBookId());

            // 3. 构建上下文内容
            StringBuilder contextBuilder = new StringBuilder();
            contextBuilder.append("书籍名称：").append(book.getTitle()).append("\n");
            contextBuilder.append("作者：").append(book.getAuthor()).append("\n\n");

            if (chapters != null && !chapters.isEmpty()) {
                contextBuilder.append("已有章节内容：\n\n");
                for (BookChapter chapter : chapters) {
                    contextBuilder.append("第").append(chapter.getChapterNum()).append("章 ")
                            .append(chapter.getTitle()).append("\n");
                    if (chapter.getContent() != null && !chapter.getContent().isEmpty()) {
                        // 限制每章内容长度，避免超出token限制
                        String content = chapter.getContent();
                        if (content.length() > 500) {
                            content = content.substring(0, 500) + "...";
                        }
                        contextBuilder.append(content).append("\n\n");
                    }
                }
            } else {
                contextBuilder.append("这是一本新书，还没有章节。\n\n");
            }

            // 4. 构建提示词
            int nextChapterNum = chapters.isEmpty() ? 1 : 
                    chapters.stream().mapToInt(BookChapter::getChapterNum).max().orElse(0) + 1;

            String prompt = buildPrompt(contextBuilder.toString(), book, nextChapterNum, dto.getPrompt());

            // 5. 调用LLM API生成内容
            String generatedContent = callLLMAPI(prompt, dto.getMaxTokens(), dto.getTemperature());

            if (generatedContent == null || generatedContent.trim().isEmpty()) {
                return ContinueWritingResultVO.builder()
                        .success(false)
                        .message("AI生成失败，返回内容为空")
                        .build();
            }

            // 6. 解析生成的内容（提取标题和内容）
            String[] parts = parseGeneratedContent(generatedContent);
            String title = parts[0];
            String content = parts[1];

            // 7. 创建新章节
            BookChapter newChapter = BookChapter.builder()
                    .bookId(dto.getBookId())
                    .title(title)
                    .chapterNum(nextChapterNum)
                    .content(content)
                    .isFree(1) // 默认免费
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();

            bookChapterMapper.insert(newChapter);

            // 8. 返回结果
            return ContinueWritingResultVO.builder()
                    .chapterId(newChapter.getId())
                    .title(title)
                    .content(content)
                    .chapterNum(nextChapterNum)
                    .success(true)
                    .message("续写成功")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ContinueWritingResultVO.builder()
                    .success(false)
                    .message("续写失败：" + e.getMessage())
                    .build();
        }
    }

    /**
     * 构建提示词
     */
    private String buildPrompt(String context, BookInfo book, int nextChapterNum, String customPrompt) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位专业的小说作家，擅长创作引人入胜的故事。\n\n");
        prompt.append("请根据以下书籍信息和已有章节内容，续写第").append(nextChapterNum).append("章。\n\n");
        prompt.append(context);
        prompt.append("\n要求：\n");
        prompt.append("1. 保持故事风格一致\n");
        prompt.append("2. 情节连贯，逻辑清晰\n");
        prompt.append("3. 语言生动，有吸引力\n");
        prompt.append("4. 章节长度适中（800-1500字）\n");
        prompt.append("5. 必须按照以下格式输出：\n");
        prompt.append("   章节标题：[标题]\n");
        prompt.append("   章节内容：[内容]\n\n");

        if (customPrompt != null && !customPrompt.trim().isEmpty()) {
            prompt.append("特殊要求：").append(customPrompt).append("\n\n");
        }

        prompt.append("现在开始创作第").append(nextChapterNum).append("章：\n");

        return prompt.toString();
    }

    /**
     * 调用LLM API
     */
    private String callLLMAPI(String prompt, Integer maxTokens, Double temperature) {
        try {
            String apiKey = llmProperties.getApiKey();
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new RuntimeException("LLM API Key未配置");
            }

            // 构建请求体
            String requestBody = buildLLMRequest(prompt, maxTokens, temperature);

            // 创建HTTP请求
            Request request = new Request.Builder()
                    .url(llmProperties.getBaseUrl() + "/chat/completions")
                    .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // 执行请求
            try (Response response = HTTP_CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("API调用失败，状态码：" + response.code());
                }

                String responseBody = response.body() != null ? response.body().string() : null;
                if (responseBody == null) {
                    throw new RuntimeException("API返回为空");
                }

                // 解析响应
                return parseLLMResponse(responseBody);
            }

        } catch (Exception e) {
            throw new RuntimeException("调用LLM API失败：" + e.getMessage(), e);
        }
    }

    /**
     * 构建LLM请求JSON
     */
    private String buildLLMRequest(String prompt, Integer maxTokens, Double temperature) throws Exception {
        com.fasterxml.jackson.databind.node.ObjectNode root = OBJECT_MAPPER.createObjectNode();
        root.put("model", llmProperties.getModel());
        
        com.fasterxml.jackson.databind.node.ArrayNode messages = OBJECT_MAPPER.createArrayNode();
        com.fasterxml.jackson.databind.node.ObjectNode message = OBJECT_MAPPER.createObjectNode();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        
        root.set("messages", messages);
        root.put("max_tokens", maxTokens);
        root.put("temperature", temperature);
        
        return OBJECT_MAPPER.writeValueAsString(root);
    }

    /**
     * 解析LLM响应
     */
    private String parseLLMResponse(String responseBody) throws Exception {
        JsonNode root = OBJECT_MAPPER.readTree(responseBody);
        JsonNode choices = root.get("choices");

        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode message = choices.get(0).get("message");
            if (message != null) {
                return message.get("content").asText();
            }
        }

        throw new RuntimeException("无法解析API响应");
    }

    /**
     * 解析生成的内容，提取标题和正文
     */
    private String[] parseGeneratedContent(String content) {
        String title = "新章节";
        String body = content;

        // 尝试提取标题
        if (content.contains("章节标题：")) {
            int titleStart = content.indexOf("章节标题：") + 5;
            int titleEnd = content.indexOf("\n", titleStart);
            if (titleEnd > titleStart) {
                title = content.substring(titleStart, titleEnd).trim();
            } else {
                title = content.substring(titleStart).trim();
            }

            // 提取内容
            if (content.contains("章节内容：")) {
                int contentStart = content.indexOf("章节内容：") + 5;
                body = content.substring(contentStart).trim();
            }
        } else if (content.contains("标题：")) {
            int titleStart = content.indexOf("标题：") + 3;
            int titleEnd = content.indexOf("\n", titleStart);
            if (titleEnd > titleStart) {
                title = content.substring(titleStart, titleEnd).trim();
            }
        }

        // 清理内容中的标记
        body = body.replaceAll("章节内容：", "").trim();

        return new String[]{title, body};
    }
}
