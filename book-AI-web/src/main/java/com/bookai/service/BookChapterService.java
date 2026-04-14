package com.bookai.service;

import com.bookai.dto.BookChapterDTO;
import com.bookai.vo.BookChapterVO;

import java.util.List;

public interface BookChapterService {

    List<BookChapterVO> getChaptersByBookId(Long bookId);

    BookChapterVO getChapterDetail(Long id);

    boolean createChapter(BookChapterDTO dto);

    boolean updateChapter(BookChapterDTO dto);

    boolean deleteChapter(Long id);
}
