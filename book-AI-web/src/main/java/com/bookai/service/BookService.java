package com.bookai.service;

import com.bookai.dto.BookDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.vo.BookVO;
import com.bookai.vo.PageResultVO;

import java.util.List;

public interface BookService {

    PageResultVO<BookVO> getBookPage(PageQueryDTO dto, Long categoryId);

    BookVO getBookDetail(Long id);

    List<BookVO> getHotBooks(Integer limit);

    boolean createBook(BookDTO dto);

    boolean updateBook(BookDTO dto);

    boolean deleteBook(Long id);

    boolean updateBookStatus(Long id, Integer status);
}
