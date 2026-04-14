package com.bookai.service;

import com.bookai.dto.UserLoginDTO;
import com.bookai.vo.UserLoginVO;

public interface AdminService {
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
