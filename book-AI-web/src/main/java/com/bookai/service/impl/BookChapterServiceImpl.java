package com.bookai.service.impl;

import com.bookai.dto.BookChapterDTO;
import com.bookai.entity.BookChapter;
import com.bookai.mapper.BookChapterMapper;
import com.bookai.service.BookChapterService;
import com.bookai.vo.BookChapterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookChapterServiceImpl implements BookChapterService {

    @Autowired
    private BookChapterMapper bookChapterMapper;

    @Override
    public List<BookChapterVO> getChaptersByBookId(Long bookId) {
        List<BookChapter> chapters = bookChapterMapper.selectByBookId(bookId);
        return chapters.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public BookChapterVO getChapterDetail(Long id) {
        BookChapter chapter = bookChapterMapper.selectById(id);
        if (chapter == null) {
            return null;
        }
        return convertToVO(chapter);
    }

    @Override
    public boolean createChapter(BookChapterDTO dto) {
        BookChapter chapter = new BookChapter();
        BeanUtils.copyProperties(dto, chapter);
        chapter.setCreateTime(LocalDateTime.now());
        chapter.setUpdateTime(LocalDateTime.now());
        return bookChapterMapper.insert(chapter) > 0;
    }

    @Override
    public boolean updateChapter(BookChapterDTO dto) {
        BookChapter chapter = new BookChapter();
        BeanUtils.copyProperties(dto, chapter);
        chapter.setUpdateTime(LocalDateTime.now());
        return bookChapterMapper.updateById(chapter) > 0;
    }

    @Override
    public boolean deleteChapter(Long id) {
        return bookChapterMapper.deleteById(id) > 0;
    }

    private BookChapterVO convertToVO(BookChapter chapter) {
        BookChapterVO vo = new BookChapterVO();
        BeanUtils.copyProperties(chapter, vo);
        return vo;
    }
}
