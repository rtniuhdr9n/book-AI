package com.bookai.service;

import com.bookai.dto.BookCategoryDTO;
import com.bookai.vo.BookCategoryVO;

import java.util.List;

public interface BookCategoryService {

    List<BookCategoryVO> getCategoryTree();

    List<BookCategoryVO> getCategoriesByParentId(Long parentId);

    boolean createCategory(BookCategoryDTO dto);

    boolean updateCategory(BookCategoryDTO dto);

    boolean deleteCategory(Long id);
}
