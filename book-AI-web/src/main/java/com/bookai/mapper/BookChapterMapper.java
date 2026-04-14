package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookai.entity.BookChapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookChapterMapper extends BaseMapper<BookChapter> {

    List<BookChapter> selectByBookId(@Param("bookId") Long bookId);

    BookChapter selectByBookIdAndChapterNum(@Param("bookId") Long bookId, 
                                             @Param("chapterNum") Integer chapterNum);
}
