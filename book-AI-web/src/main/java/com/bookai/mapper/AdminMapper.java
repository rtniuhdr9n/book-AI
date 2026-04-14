package com.bookai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookai.entity.User;
import jakarta.validation.constraints.NotBlank;

public interface AdminMapper extends BaseMapper<User> {
    User selectByUsername(@NotBlank(message = "用户名不能为空") String username);
}
