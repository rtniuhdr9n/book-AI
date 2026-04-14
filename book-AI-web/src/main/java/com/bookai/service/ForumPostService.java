package com.bookai.service;

import com.bookai.dto.ForumPostDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.vo.ForumPostVO;
import com.bookai.vo.PageResultVO;

public interface ForumPostService {

    PageResultVO<ForumPostVO> getPostsBySection(Long sectionId, PageQueryDTO dto);

    PageResultVO<ForumPostVO> getPostsByUser(Long userId, PageQueryDTO dto);

    ForumPostVO getPostDetail(Long id);

    boolean createPost(Long userId, ForumPostDTO dto);

    boolean updatePost(Long userId, ForumPostDTO dto);

    boolean deletePost(Long userId, Long postId);

    boolean increaseViewCount(Long postId);

    boolean likePost(Long postId);

    // Admin
    boolean setTop(Long postId, Integer isTop);

    boolean setEssence(Long postId, Integer isEssence);

    boolean deletePostByAdmin(Long postId);
}
