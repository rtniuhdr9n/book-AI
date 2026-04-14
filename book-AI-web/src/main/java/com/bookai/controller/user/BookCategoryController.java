package com.bookai.controller.user;

import com.bookai.common.Result;
import com.bookai.service.BookCategoryService;
import com.bookai.vo.BookCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/category")
public class BookCategoryController {

    @Autowired
    private BookCategoryService bookCategoryService;

    @GetMapping("/tree")
    public Result tree() {
        List<BookCategoryVO> list = bookCategoryService.getCategoryTree();
        return Result.success(list);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Long parentId) {
        List<BookCategoryVO> list = bookCategoryService.getCategoriesByParentId(parentId);
        return Result.success(list);
    }
}
