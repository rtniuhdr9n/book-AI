package com.bookai.controller.admin;

import com.bookai.common.Result;
import com.bookai.dto.ContinueWritingDTO;
import com.bookai.service.AiWritingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员AI续写接口", description = "管理端AI续写相关接口")
@RestController
@RequestMapping("/admin/book")
public class AdminAiWritingController {

    @Autowired
    private AiWritingService aiWritingService;

    @Operation(summary = "AI续写书籍章节（管理员）")
    @PostMapping("/continue-writing")
    public Result continueWriting(@Valid @RequestBody ContinueWritingDTO dto) {
        return Result.success(aiWritingService.continueWriting(dto));
    }
}
