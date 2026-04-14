package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookai.entity.BookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    Page<BookInfo> selectPageByCategory(@Param("page") Page<BookInfo> page, 
                                         @Param("categoryId") Long categoryId);

    List<BookInfo> selectHotBooks(@Param("limit") Integer limit);

    List<BookInfo> selectByIds(@Param("ids") List<Long> ids);

    int decreaseStock(@Param("bookId") Long bookId, @Param("quantity") Integer quantity);

    int increaseSales(@Param("bookId") Long bookId, @Param("quantity") Integer quantity);
}
