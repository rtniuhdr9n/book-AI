package com.bookai.controller.admin;

import com.bookai.common.Result;
import com.bookai.dto.ForumSectionDTO;
import com.bookai.service.ForumPostService;
import com.bookai.service.ForumSectionService;
import com.bookai.vo.ForumSectionVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/forum")
public class AdminForumController {

    @Autowired
    private ForumSectionService forumSectionService;

    @Autowired
    private ForumPostService forumPostService;

    // 板块管理
    @GetMapping("/sections")
    public Result sections() {
        List<ForumSectionVO> list = forumSectionService.getAllSections();
        return Result.success(list);
    }

    @GetMapping("/section/{id}")
    public Result sectionDetail(@PathVariable Long id) {
        ForumSectionVO vo = forumSectionService.getSectionDetail(id);
        if (vo == null) {
            return Result.error("板块不存在");
        }
        return Result.success(vo);
    }

    @PostMapping("/section/create")
    public Result createSection(@Valid @RequestBody ForumSectionDTO dto) {
        boolean success = forumSectionService.createSection(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("创建失败");
    }

    @PutMapping("/section/update")
    public Result updateSection(@Valid @RequestBody ForumSectionDTO dto) {
        boolean success = forumSectionService.updateSection(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/section/delete/{id}")
    public Result deleteSection(@PathVariable Long id) {
        boolean success = forumSectionService.deleteSection(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    // 帖子管理
    @PutMapping("/post/top/{id}")
    public Result setTop(@PathVariable Long id, @RequestParam Integer isTop) {
        boolean success = forumPostService.setTop(id, isTop);
        if (success) {
            return Result.success();
        }
        return Result.error("操作失败");
    }

    @PutMapping("/post/essence/{id}")
    public Result setEssence(@PathVariable Long id, @RequestParam Integer isEssence) {
        boolean success = forumPostService.setEssence(id, isEssence);
        if (success) {
            return Result.success();
        }
        return Result.error("操作失败");
    }

    @DeleteMapping("/post/delete/{id}")
    public Result deletePost(@PathVariable Long id) {
        boolean success = forumPostService.deletePostByAdmin(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
