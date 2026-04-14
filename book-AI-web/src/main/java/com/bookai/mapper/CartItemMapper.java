package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookai.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {

    List<CartItem> selectByUserId(@Param("userId") Long userId);

    CartItem selectByUserIdAndBookId(@Param("userId") Long userId, 
                                      @Param("bookId") Long bookId);

    int deleteByUserIdAndIds(@Param("userId") Long userId, 
                             @Param("ids") List<Long> ids);

    int clearByUserId(@Param("userId") Long userId);
}
