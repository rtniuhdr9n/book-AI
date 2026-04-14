package com.bookai.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookai.Utils.RedisUtil;
import com.bookai.dto.BookDTO;
import com.bookai.dto.PageQueryDTO;
import com.bookai.entity.BookCategory;
import com.bookai.entity.BookInfo;
import com.bookai.mapper.BookCategoryMapper;
import com.bookai.mapper.BookInfoMapper;
import com.bookai.service.BookService;
import com.bookai.vo.BookVO;
import com.bookai.vo.PageResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String BOOK_DETAIL_CACHE_KEY = "book:detail:";
    private static final String BOOK_HOT_LIST_CACHE_KEY = "book:hot:list";
    private static final long CACHE_EXPIRE_HOURS = 1;

    @Override
    public PageResultVO<BookVO> getBookPage(PageQueryDTO dto, Long categoryId) {
        Page<BookInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<BookInfo> resultPage = bookInfoMapper.selectPageByCategory(page, categoryId);

        List<BookVO> voList = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResultVO.of(resultPage.getTotal(), dto.getPageNum(), dto.getPageSize(), voList);
    }

    @Override
    public BookVO getBookDetail(Long id) {
        // 先从缓存获取
        String cacheKey = BOOK_DETAIL_CACHE_KEY + id;
        BookVO cachedVO = (BookVO) redisUtil.get(cacheKey);
        if (cachedVO != null) {
            return cachedVO;
        }

        // 缓存未命中，查询数据库
        BookInfo book = bookInfoMapper.selectById(id);
        if (book == null) {
            return null;
        }
        BookVO vo = convertToVO(book);

        // 写入缓存，1小时过期
        redisUtil.set(cacheKey, vo, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);

        return vo;
    }

    @Override
    public List<BookVO> getHotBooks(Integer limit) {
        // 先从缓存获取
        Object cachedList = redisUtil.get(BOOK_HOT_LIST_CACHE_KEY);
        if (cachedList instanceof List) {
            return (List<BookVO>) cachedList;
        }

        // 缓存未命中，查询数据库
        List<BookInfo> books = bookInfoMapper.selectHotBooks(limit);
        List<BookVO> voList = books.stream().map(this::convertToVO).collect(Collectors.toList());

        // 写入缓存，1小时过期
        redisUtil.set(BOOK_HOT_LIST_CACHE_KEY, voList, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);

        return voList;
    }

    @Override
    public boolean createBook(BookDTO dto) {
        BookInfo book = new BookInfo();
        BeanUtils.copyProperties(dto, book);
        book.setSales(0);
        book.setIsOnSale(1);
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        boolean success = bookInfoMapper.insert(book) > 0;
        
        // 清除热门书籍缓存
        if (success) {
            redisUtil.delete(BOOK_HOT_LIST_CACHE_KEY);
        }
        
        return success;
    }

    @Override
    public boolean updateBook(BookDTO dto) {
        BookInfo book = new BookInfo();
        BeanUtils.copyProperties(dto, book);
        book.setUpdateTime(LocalDateTime.now());
        boolean success = bookInfoMapper.updateById(book) > 0;
        
        // 清除相关缓存
        if (success && dto.getId() != null) {
            redisUtil.delete(BOOK_DETAIL_CACHE_KEY + dto.getId());
            redisUtil.delete(BOOK_HOT_LIST_CACHE_KEY);
        }
        
        return success;
    }

    @Override
    public boolean deleteBook(Long id) {
        boolean success = bookInfoMapper.deleteById(id) > 0;
        
        // 清除相关缓存
        if (success) {
            redisUtil.delete(BOOK_DETAIL_CACHE_KEY + id);
            redisUtil.delete(BOOK_HOT_LIST_CACHE_KEY);
        }
        
        return success;
    }

    @Override
    public boolean updateBookStatus(Long id, Integer status) {
        BookInfo book = new BookInfo();
        book.setId(id);
        book.setIsOnSale(status);
        book.setUpdateTime(LocalDateTime.now());
        return bookInfoMapper.updateById(book) > 0;
    }

    private BookVO convertToVO(BookInfo book) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        // 设置分类名称
        if (book.getCategoryId() != null) {
            BookCategory category = bookCategoryMapper.selectById(book.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
        return vo;
    }
}
