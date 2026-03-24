package com.bookai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
@Builder
public class User {
    private  Long id;
    private  String username;
    private  String password;
    private  String phone;
    private  LocalDateTime createTime;
    private  LocalDateTime updateTime;
}
