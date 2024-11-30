package com.example.hobiday_backend.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String code;
    private String message;
    private T result;

    private final static String SUCCESS_CODE = "200";

    public static <T> ApiResponse<T> success(T result) {
        return ApiResponse.<T>builder()
                .code(SUCCESS_CODE)
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> success(T result, String message) {
        return ApiResponse.<T>builder()
                .result(result)
                .message(message)
                .code(SUCCESS_CODE)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .code(SUCCESS_CODE)
                .build();
    }
}
