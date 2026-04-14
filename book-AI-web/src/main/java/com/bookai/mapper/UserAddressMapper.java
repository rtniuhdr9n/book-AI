package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookai.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    List<UserAddress> selectByUserId(@Param("userId") Long userId);

    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int clearDefaultByUserId(@Param("userId") Long userId);
}
