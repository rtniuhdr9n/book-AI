package com.bookai.controller.user;

import com.bookai.common.Result;
import com.bookai.dto.PageQueryDTO;
import com.bookai.service.BookChapterService;
import com.bookai.service.BookService;
import com.bookai.vo.BookChapterVO;
import com.bookai.vo.BookVO;
import com.bookai.vo.PageResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookChapterService bookChapterService;

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

    @GetMapping("/hot")
    public Result hot(@RequestParam(defaultValue = "10") Integer limit) {
        List<BookVO> list = bookService.getHotBooks(limit);
        return Result.success(list);
    }

    @GetMapping("/chapters/{bookId}")
    public Result chapters(@PathVariable Long bookId) {
        List<BookChapterVO> list = bookChapterService.getChaptersByBookId(bookId);
        return Result.success(list);
    }

    @GetMapping("/chapter/{id}")
    public Result chapterDetail(@PathVariable Long id) {
        BookChapterVO vo = bookChapterService.getChapterDetail(id);
        if (vo == null) {
            return Result.error("章节不存在");
        }
        return Result.success(vo);
    }
}
