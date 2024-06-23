package com.bellintegrator.spring_mvc_homework.controller;

import com.bellintegrator.spring_mvc_homework.exceptionhandling.ExceptionInfo;
import com.bellintegrator.spring_mvc_homework.model.dto.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof ExceptionInfo) {
            return BaseResponse.fail((ExceptionInfo) body);
        }
        return BaseResponse.success(body);
    }
}
