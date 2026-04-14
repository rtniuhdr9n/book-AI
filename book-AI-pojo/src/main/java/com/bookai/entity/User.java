package com.bookai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("\"user\"")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private  Long id;

    private  String username;
    private  String password;
    private  String phone;
    private  Integer role; // 角色：0-USER(普通用户), 1-ADMIN(管理员), 2-VIP1, 3-VIP2, 4-VIP3
    private  LocalDateTime createTime;
    private  LocalDateTime updateTime;
}
