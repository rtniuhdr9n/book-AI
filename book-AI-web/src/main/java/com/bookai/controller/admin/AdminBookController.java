package com.bookai.controller.admin;

import com.bookai.common.Result;
import com.bookai.dto.BookCategoryDTO;
import com.bookai.dto.BookChapterDTO;
import com.bookai.dto.BookDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.service.BookCategoryService;
import com.bookai.service.BookChapterService;
import com.bookai.service.BookService;
import com.bookai.vo.BookCategoryVO;
import com.bookai.vo.BookChapterVO;
import com.bookai.vo.BookVO;
import com.bookai.vo.PageResultVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/book")
public class AdminBookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookCategoryService bookCategoryService;

    @Autowired
    private BookChapterService bookChapterService;

    // 书籍管理
    @GetMapping("/list")
    public Result list(PageQueryDTO dto, @RequestParam(required = false) Long categoryId) {
        PageResultVO<BookVO> result = bookService.getBookPage(dto, categoryId);
        return Result.success(result);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {
        BookVO vo = bookService.getBookDetail(id);
        if (vo == null) {
            return Result.error("书籍不存在");
        }
        return Result.success(vo);
    }

    @PostMapping("/create")
    public Result create(@Valid @RequestBody BookDTO dto) {
        boolean success = bookService.createBook(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("创建失败");
    }

    @PutMapping("/update")
    public Result update(@Valid @RequestBody BookDTO dto) {
        boolean success = bookService.updateBook(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean success = bookService.deleteBook(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @PutMapping("/status/{id}")
    public Result updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = bookService.updateBookStatus(id, status);
        if (success) {
            return Result.success();
        }
        return Result.error("更新状态失败");
    }

    // 分类管理
    @GetMapping("/category/tree")
    public Result categoryTree() {
        List<BookCategoryVO> list = bookCategoryService.getCategoryTree();
        return Result.success(list);
    }

    @PostMapping("/category/create")
    public Result createCategory(@Valid @RequestBody BookCategoryDTO dto) {
        boolean success = bookCategoryService.createCategory(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("创建失败");
    }

    @PutMapping("/category/update")
    public Result updateCategory(@Valid @RequestBody BookCategoryDTO dto) {
        boolean success = bookCategoryService.updateCategory(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/category/delete/{id}")
    public Result deleteCategory(@PathVariable Long id) {
        boolean success = bookCategoryService.deleteCategory(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    // 章节管理
    @GetMapping("/chapters/{bookId}")
    public Result chapters(@PathVariable Long bookId) {
        List<BookChapterVO> list = bookChapterService.getChaptersByBookId(bookId);
        return Result.success(list);
    }

    @PostMapping("/chapter/create")
    public Result createChapter(@Valid @RequestBody BookChapterDTO dto) {
        boolean success = bookChapterService.createChapter(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("创建失败");
    }

    @PutMapping("/chapter/update")
    public Result updateChapter(@Valid @RequestBody BookChapterDTO dto) {
        boolean success = bookChapterService.updateChapter(dto);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/chapter/delete/{id}")
    public Result deleteChapter(@PathVariable Long id) {
        boolean success = bookChapterService.deleteChapter(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
