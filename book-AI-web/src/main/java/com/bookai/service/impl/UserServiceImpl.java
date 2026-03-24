package com.bookai.service.impl;

import com.bookai.Utils.JwtUtil;
import com.bookai.dto.UserLoginDTO;
import com.bookai.dto.UserRegisterDTO;
import com.bookai.entity.User;
import com.bookai.mapper.UserMapper;
import com.bookai.service.UserService;
import com.bookai.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean register(UserRegisterDTO userRegisterDTO)
    {
        boolean existingUser=userMapper.existsByUsername(userRegisterDTO.getUsername());

        if(!existingUser) {
            String encodePassword=encoder.encode(userRegisterDTO.getPassword());
            LocalDateTime time = LocalDateTime.now();
            User user = User.builder()
                    .username(userRegisterDTO.getUsername())
                    .password(encodePassword)
                    .phone(userRegisterDTO.getPhone())
                    .createTime(time)
                    .updateTime(time)
                    .build();
            return userMapper.insert(user) > 0;
        }
        else{
            return false;
        }
    }

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO){
        User user=userMapper.selectByUsername(userLoginDTO.getUsername());
        boolean ok= encoder.matches(userLoginDTO.getPassword(),user.getPassword());
        if(!ok){
            return null;
        }else{
            String token= jwtUtil.generateToken(user.getId(), user.getUsername());
            return UserLoginVO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .authorization(token)
                    .build();
        }
    }







}
