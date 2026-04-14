package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookai.entity.BookCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookCategoryMapper extends BaseMapper<BookCategory> {

    List<BookCategory> selectByParentId(@Param("parentId") Long parentId);

    List<BookCategory> selectAllTree();
}
