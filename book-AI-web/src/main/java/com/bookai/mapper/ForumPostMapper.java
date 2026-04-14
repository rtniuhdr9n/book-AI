package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookai.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {

    Page<ForumPost> selectPageBySectionId(@Param("page") Page<ForumPost> page, 
                                           @Param("sectionId") Long sectionId);

    Page<ForumPost> selectPageByUserId(@Param("page") Page<ForumPost> page, 
                                        @Param("userId") Long userId);

    @Update("UPDATE forum_post SET view_count = view_count + 1 WHERE id = #{postId}")
    int increaseViewCount(@Param("postId") Long postId);

    @Update("UPDATE forum_post SET reply_count = reply_count + 1 WHERE id = #{postId}")
    int increaseReplyCount(@Param("postId") Long postId);

    @Update("UPDATE forum_post SET like_count = like_count + 1 WHERE id = #{postId}")
    int increaseLikeCount(@Param("postId") Long postId);
}
