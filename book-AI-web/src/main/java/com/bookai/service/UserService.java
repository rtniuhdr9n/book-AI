package com.bookai.service;

import com.bookai.common.Result;
import com.bookai.dto.UserRegisterDTO;

public interface UserService {

    Result register(UserRegisterDTO userRegisterDTO);
}
