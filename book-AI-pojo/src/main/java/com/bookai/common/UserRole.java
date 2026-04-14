package com.bookai.common;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN(0, "admin"),

    USER(1, "user");


    private final int code;
    private final String description;

    UserRole(int code, String description) {
        this.code = code;
        this.description = description;
    }


}