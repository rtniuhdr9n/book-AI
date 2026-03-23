package com.bookai.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result{
    private Integer code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        return Result.builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static Result success(){
        return Result.builder()
                .code(200)
                .message("Success")
                .data(null)
                .build();
    }

    public static Result error(String message){
        return Result.builder()
                .code(500)
                .message(message)
                .data(null)
                .build();
    }



}
