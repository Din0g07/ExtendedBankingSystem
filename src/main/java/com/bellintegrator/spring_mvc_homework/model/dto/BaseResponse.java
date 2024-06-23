package com.bellintegrator.spring_mvc_homework.model.dto;

import com.bellintegrator.spring_mvc_homework.exceptionhandling.ExceptionInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private boolean status;
    private T body;
    private ExceptionInfo error;

    public static <T> BaseResponse<T> success(T body) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.status = true;
        baseResponse.body = body;
        return baseResponse;
    }

    public static <T> BaseResponse<T> fail(ExceptionInfo error) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.status = true;
        baseResponse.error = error;
        return baseResponse;
    }
}
