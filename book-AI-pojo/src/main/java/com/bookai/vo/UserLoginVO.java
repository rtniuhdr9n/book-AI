package com.bookai.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {
    private  Long id;
    private  String username;
    private  Integer role; // 角色：0-USER(普通用户), 1-ADMIN(管理员), 2-VIP1, 3-VIP2, 4-VIP3
    private  String authorization;
}
