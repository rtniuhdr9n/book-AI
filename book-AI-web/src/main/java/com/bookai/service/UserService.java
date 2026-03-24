package com.bookai.service;

import com.bookai.dto.UserLoginDTO;
import com.bookai.dto.UserRegisterDTO;
import com.bookai.vo.UserLoginVO;

public interface UserService {

   UserLoginVO login(UserLoginDTO userLoginDTO);

    boolean register(UserRegisterDTO userRegisterDTO);
}
