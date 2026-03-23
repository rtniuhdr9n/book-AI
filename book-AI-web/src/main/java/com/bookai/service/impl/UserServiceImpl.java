package com.bookai.service.impl;

import com.bookai.common.Result;
import com.bookai.dto.UserRegisterDTO;
import com.bookai.mapper.UserMapper;
import com.bookai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper UserMapper;

    @Override
    public Result register(UserRegisterDTO userRegisterDTO) {
        return null;
    }
}
