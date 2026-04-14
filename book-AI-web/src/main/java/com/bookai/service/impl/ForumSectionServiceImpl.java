package com.bookai.service.impl;

import com.bookai.dto.ForumSectionDTO;
import com.bookai.entity.ForumSection;
import com.bookai.mapper.ForumSectionMapper;
import com.bookai.service.ForumSectionService;
import com.bookai.vo.ForumSectionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumSectionServiceImpl implements ForumSectionService {

    @Autowired
    private ForumSectionMapper forumSectionMapper;

    @Override
    public List<ForumSectionVO> getAllSections() {
        List<ForumSection> sections = forumSectionMapper.selectList(null);
        return sections.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public ForumSectionVO getSectionDetail(Long id) {
        ForumSection section = forumSectionMapper.selectById(id);
        if (section == null) {
            return null;
        }
        return convertToVO(section);
    }

    @Override
    public boolean createSection(ForumSectionDTO dto) {
        ForumSection section = new ForumSection();
        BeanUtils.copyProperties(dto, section);
        section.setPostCount(0);
        section.setCreateTime(LocalDateTime.now());
        section.setUpdateTime(LocalDateTime.now());
        return forumSectionMapper.insert(section) > 0;
    }

    @Override
    public boolean updateSection(ForumSectionDTO dto) {
        ForumSection section = new ForumSection();
        BeanUtils.copyProperties(dto, section);
        section.setUpdateTime(LocalDateTime.now());
        return forumSectionMapper.updateById(section) > 0;
    }

    @Override
    public boolean deleteSection(Long id) {
        return forumSectionMapper.deleteById(id) > 0;
    }

    private ForumSectionVO convertToVO(ForumSection section) {
        ForumSectionVO vo = new ForumSectionVO();
        BeanUtils.copyProperties(section, vo);
        return vo;
    }
}
