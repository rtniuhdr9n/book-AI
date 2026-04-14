package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookai.entity.ForumSection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ForumSectionMapper extends BaseMapper<ForumSection> {

    @Update("UPDATE forum_section SET post_count = post_count + 1 WHERE id = #{sectionId}")
    int increasePostCount(@Param("sectionId") Long sectionId);

    @Update("UPDATE forum_section SET post_count = post_count - 1 WHERE id = #{sectionId} AND post_count > 0")
    int decreasePostCount(@Param("sectionId") Long sectionId);
}
