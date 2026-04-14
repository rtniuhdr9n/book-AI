package com.bookai.controller;

import com.bookai.common.Result;
import com.bookai.dto.UserLoginDTO;
import com.bookai.service.AdminService;
import com.bookai.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = adminService.login(userLoginDTO);
        if (userLoginVO != null) {
            return Result.success(userLoginVO);
        } else {
            return Result.error("用户名或密码错误");
        }
    }



}