package com.bookai.service.impl;

import com.bookai.dto.BookCategoryDTO;
import com.bookai.entity.BookCategory;
import com.bookai.mapper.BookCategoryMapper;
import com.bookai.service.BookCategoryService;
import com.bookai.vo.BookCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookCategoryServiceImpl implements BookCategoryService {

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Override
    public List<BookCategoryVO> getCategoryTree() {
        List<BookCategory> categories = bookCategoryMapper.selectAllTree();
        return buildTree(categories, 0L);
    }

    @Override
    public List<BookCategoryVO> getCategoriesByParentId(Long parentId) {
        List<BookCategory> categories = bookCategoryMapper.selectByParentId(parentId);
        return categories.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public boolean createCategory(BookCategoryDTO dto) {
        BookCategory category = new BookCategory();
        BeanUtils.copyProperties(dto, category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        return bookCategoryMapper.insert(category) > 0;
    }

    @Override
    public boolean updateCategory(BookCategoryDTO dto) {
        BookCategory category = new BookCategory();
        BeanUtils.copyProperties(dto, category);
        category.setUpdateTime(LocalDateTime.now());
        return bookCategoryMapper.updateById(category) > 0;
    }

    @Override
    public boolean deleteCategory(Long id) {
        // 检查是否有子分类
        List<BookCategory> children = bookCategoryMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }
        return bookCategoryMapper.deleteById(id) > 0;
    }

    private List<BookCategoryVO> buildTree(List<BookCategory> categories, Long parentId) {
        Map<Long, List<BookCategory>> parentMap = categories.stream()
                .collect(Collectors.groupingBy(BookCategory::getParentId));

        List<BookCategoryVO> result = new ArrayList<>();
        List<BookCategory> list = parentMap.getOrDefault(parentId, new ArrayList<>());

        for (BookCategory category : list) {
            BookCategoryVO vo = convertToVO(category);
            vo.setChildren(buildTree(categories, category.getId()));
            result.add(vo);
        }
        return result;
    }

    private BookCategoryVO convertToVO(BookCategory category) {
        BookCategoryVO vo = new BookCategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}
