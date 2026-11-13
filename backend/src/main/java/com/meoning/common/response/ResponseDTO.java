package com.meoning.common.response;


import com.meoning.common.error.ErrorCode;

public record ResponseDTO<T>(String code, String message, T data) {

    private static final String SUCCESS_CODE = "SUCCESS";

    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(SUCCESS_CODE, message, data);
    }

    public static ResponseDTO<Void> success(String message) {
        return new ResponseDTO<>(SUCCESS_CODE, message, null);
    }

    public static <T> ResponseDTO<T> error(ErrorCode errorCode, T data) {
        return new ResponseDTO<>(errorCode.name(), errorCode.getMessage(), data);
    }
}
