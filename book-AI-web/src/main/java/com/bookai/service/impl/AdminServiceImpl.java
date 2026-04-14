package com.bookai.service.impl;

import com.bookai.Utils.JwtUtil;
import com.bookai.common.UserRole;
import com.bookai.dto.UserLoginDTO;
import com.bookai.entity.User;
import com.bookai.mapper.AdminMapper;
import com.bookai.service.AdminService;
import com.bookai.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;


        private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        @Autowired
        private JwtUtil jwtUtil;

        @Override
        public UserLoginVO login(UserLoginDTO userLoginDTO) {
            User user = adminMapper.selectByUsername(userLoginDTO.getUsername());

            if (user == null) {
                return null;
            }

            boolean ok = encoder.matches(userLoginDTO.getPassword(), user.getPassword());
            if (!ok) {
                return null;
            }

            if (user.getRole() != UserRole.ADMIN.getCode()) {
                return null;
            }

            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
            return UserLoginVO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .authorization(token)
                    .build();
        }
}

