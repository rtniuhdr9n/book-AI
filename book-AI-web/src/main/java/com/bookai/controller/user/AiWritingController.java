package com.bookai.controller.user;

import com.bookai.common.Result;
import com.bookai.dto.ContinueWritingDTO;
import com.bookai.service.AiWritingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI续写接口", description = "用户端AI续写相关接口")
@RestController
@RequestMapping("/user/book")
public class AiWritingController {

    @Autowired
    private AiWritingService aiWritingService;

    @Operation(summary = "AI续写书籍章节")
    @PostMapping("/continue-writing")
    public Result continueWriting(@Valid @RequestBody ContinueWritingDTO dto) {
        return Result.success(aiWritingService.continueWriting(dto));
    }
}
