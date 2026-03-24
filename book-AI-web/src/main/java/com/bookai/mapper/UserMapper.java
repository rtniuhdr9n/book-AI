package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookai.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    boolean existsByUsername(@NotBlank(message = "用户名不能为空") String username);


    User selectByUsername(@NotBlank(message = "用户名不能为空") String username);
}
