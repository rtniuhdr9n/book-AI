package com.bookai.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookai.Utils.RedisUtil;
import com.bookai.dto.ForumPostDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.entity.ForumPost;
import com.bookai.entity.ForumSection;
import com.bookai.entity.User;
import com.bookai.mapper.ForumPostMapper;
import com.bookai.mapper.ForumSectionMapper;
import com.bookai.mapper.UserMapper;
import com.bookai.service.ForumPostService;
import com.bookai.vo.ForumPostVO;
import com.bookai.vo.PageResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class ForumPostServiceImpl implements ForumPostService {

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private ForumSectionMapper forumSectionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String FORUM_SECTION_POSTS_KEY = "forum:section:";
    private static final long CACHE_EXPIRE_MINUTES = 30;

    @Override
    public PageResultVO<ForumPostVO> getPostsBySection(Long sectionId, PageQueryDTO dto) {
        // 尝试从缓存获取（使用分页参数作为key的一部分）
        String cacheKey = FORUM_SECTION_POSTS_KEY + sectionId + ":page:" + dto.getPageNum() + ":size:" + dto.getPageSize();
        Object cachedResult = redisUtil.get(cacheKey);
        if (cachedResult instanceof PageResultVO) {
            return (PageResultVO<ForumPostVO>) cachedResult;
        }

        Page<ForumPost> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<ForumPost> resultPage = forumPostMapper.selectPageBySectionId(page, sectionId);

        PageResultVO<ForumPostVO> result = PageResultVO.of(resultPage.getTotal(), dto.getPageNum(), dto.getPageSize(),
                resultPage.getRecords().stream().map(this::convertToVO).toList());

        // 写入缓存，30分钟过期
        redisUtil.set(cacheKey, result, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        return result;
    }

    @Override
    public PageResultVO<ForumPostVO> getPostsByUser(Long userId, PageQueryDTO dto) {
        Page<ForumPost> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<ForumPost> resultPage = forumPostMapper.selectPageByUserId(page, userId);

        return PageResultVO.of(resultPage.getTotal(), dto.getPageNum(), dto.getPageSize(),
                resultPage.getRecords().stream().map(this::convertToVO).toList());
    }

    @Override
    public ForumPostVO getPostDetail(Long id) {
        ForumPost post = forumPostMapper.selectById(id);
        if (post == null) {
            return null;
        }
        return convertToVO(post);
    }

    @Override
    public boolean createPost(Long userId, ForumPostDTO dto) {
        ForumSection section = forumSectionMapper.selectById(dto.getSectionId());
        if (section == null) {
            throw new RuntimeException("板块不存在");
        }

        ForumPost post = new ForumPost();
        BeanUtils.copyProperties(dto, post);
        post.setUserId(userId);
        post.setViewCount(0);
        post.setReplyCount(0);
        post.setLikeCount(0);
        post.setIsTop(0);
        post.setIsEssence(0);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());

        int result = forumPostMapper.insert(post);
        if (result > 0) {
            forumSectionMapper.increasePostCount(dto.getSectionId());
            
            // 清除该板块的帖子列表缓存
            clearSectionPostsCache(dto.getSectionId());
        }
        return result > 0;
    }

    @Override
    public boolean updatePost(Long userId, ForumPostDTO dto) {
        ForumPost existing = forumPostMapper.selectById(dto.getId());
        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }

        ForumPost post = new ForumPost();
        post.setId(dto.getId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUpdateTime(LocalDateTime.now());
        
        boolean success = forumPostMapper.updateById(post) > 0;
        
        // 清除该板块的帖子列表缓存
        if (success) {
            clearSectionPostsCache(existing.getSectionId());
        }
        
        return success;
    }

    @Override
    public boolean deletePost(Long userId, Long postId) {
        ForumPost post = forumPostMapper.selectById(postId);
        if (post == null || !post.getUserId().equals(userId)) {
            return false;
        }

        int result = forumPostMapper.deleteById(postId);
        if (result > 0) {
            forumSectionMapper.decreasePostCount(post.getSectionId());
            
            // 清除该板块的帖子列表缓存
            clearSectionPostsCache(post.getSectionId());
        }
        return result > 0;
    }

    @Override
    public boolean increaseViewCount(Long postId) {
        return forumPostMapper.increaseViewCount(postId) > 0;
    }

    @Override
    public boolean likePost(Long postId) {
        return forumPostMapper.increaseLikeCount(postId) > 0;
    }

    @Override
    public boolean setTop(Long postId, Integer isTop) {
        ForumPost post = new ForumPost();
        post.setId(postId);
        post.setIsTop(isTop);
        post.setUpdateTime(LocalDateTime.now());
        return forumPostMapper.updateById(post) > 0;
    }

    @Override
    public boolean setEssence(Long postId, Integer isEssence) {
        ForumPost post = new ForumPost();
        post.setId(postId);
        post.setIsEssence(isEssence);
        post.setUpdateTime(LocalDateTime.now());
        return forumPostMapper.updateById(post) > 0;
    }

    @Override
    public boolean deletePostByAdmin(Long postId) {
        ForumPost post = forumPostMapper.selectById(postId);
        if (post == null) {
            return false;
        }

        int result = forumPostMapper.deleteById(postId);
        if (result > 0) {
            forumSectionMapper.decreasePostCount(post.getSectionId());
            
            // 清除该板块的帖子列表缓存
            clearSectionPostsCache(post.getSectionId());
        }
        return result > 0;
    }

    /**
     * 清除板块帖子列表缓存
     */
    private void clearSectionPostsCache(Long sectionId) {
        // 由于缓存key包含分页参数，需要使用模式删除
        // 简单做法：删除所有该板块的缓存（实际项目中可以使用Redis keys命令或更好的策略）
        String pattern = FORUM_SECTION_POSTS_KEY + sectionId + ":*";
        // 注意：这里简化处理，实际应该遍历删除匹配的key
        // 为了性能考虑，可以设置较短的缓存时间让其自然过期
        for (int pageNum = 1; pageNum <= 100; pageNum++) {
            for (int pageSize : new int[]{10, 20, 50}) {
                String cacheKey = FORUM_SECTION_POSTS_KEY + sectionId + ":page:" + pageNum + ":size:" + pageSize;
                redisUtil.delete(cacheKey);
            }
        }
    }

    private ForumPostVO convertToVO(ForumPost post) {
        ForumPostVO vo = new ForumPostVO();
        BeanUtils.copyProperties(post, vo);

        // 获取板块名称
        ForumSection section = forumSectionMapper.selectById(post.getSectionId());
        if (section != null) {
            vo.setSectionName(section.getName());
        }

        // 获取用户信息
        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }

        return vo;
    }
}
