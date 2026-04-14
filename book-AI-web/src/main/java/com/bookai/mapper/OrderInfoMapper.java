package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookai.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    OrderInfo selectByOrderNo(@Param("orderNo") String orderNo);

    Page<OrderInfo> selectPageByUserId(@Param("page") Page<OrderInfo> page, 
                                        @Param("userId") Long userId);

    List<OrderInfo> selectByStatus(@Param("status") Integer status);
}
