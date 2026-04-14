package com.bookai.controller.user;

import com.bookai.common.Result;
import com.bookai.dto.ForumPostDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.service.ForumPostService;
import com.bookai.service.ForumSectionService;
import com.bookai.vo.ForumPostVO;
import com.bookai.vo.ForumSectionVO;
import com.bookai.vo.PageResultVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/forum")
public class ForumController {

    @Autowired
    private ForumSectionService forumSectionService;

    @Autowired
    private ForumPostService forumPostService;

    // 板块相关
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

    // 帖子相关
    @GetMapping("/posts")
    public Result posts(@RequestParam Long sectionId, PageQueryDTO dto) {
        PageResultVO<ForumPostVO> result = forumPostService.getPostsBySection(sectionId, dto);
        return Result.success(result);
    }

    @GetMapping("/post/{id}")
    public Result postDetail(@PathVariable Long id) {
        ForumPostVO vo = forumPostService.getPostDetail(id);
        if (vo == null) {
            return Result.error("帖子不存在");
        }
        // 增加浏览量
        forumPostService.increaseViewCount(id);
        return Result.success(vo);
    }

    @PostMapping("/post/create")
    public Result createPost(@RequestAttribute("userId") Long userId, @Valid @RequestBody ForumPostDTO dto) {
        boolean success = forumPostService.createPost(userId, dto);
        if (success) {
            return Result.success();
        }
        return Result.error("发布失败");
    }

    @PutMapping("/post/update")
    public Result updatePost(@RequestAttribute("userId") Long userId, @Valid @RequestBody ForumPostDTO dto) {
        boolean success = forumPostService.updatePost(userId, dto);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/post/delete/{id}")
    public Result deletePost(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        boolean success = forumPostService.deletePost(userId, id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @PostMapping("/post/like/{id}")
    public Result likePost(@PathVariable Long id) {
        boolean success = forumPostService.likePost(id);
        if (success) {
            return Result.success();
        }
        return Result.error("点赞失败");
    }

    @GetMapping("/myPosts")
    public Result myPosts(@RequestAttribute("userId") Long userId, PageQueryDTO dto) {
        PageResultVO<ForumPostVO> result = forumPostService.getPostsByUser(userId, dto);
        return Result.success(result);
    }
}
