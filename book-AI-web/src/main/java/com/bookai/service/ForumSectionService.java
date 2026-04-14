package com.bookai.service;

import com.bookai.dto.ForumSectionDTO;
import com.bookai.vo.ForumSectionVO;

import java.util.List;

public interface ForumSectionService {

    List<ForumSectionVO> getAllSections();

    ForumSectionVO getSectionDetail(Long id);

    boolean createSection(ForumSectionDTO dto);

    boolean updateSection(ForumSectionDTO dto);

    boolean deleteSection(Long id);
}
