package com.bellintegrator.spring_mvc_homework.exceptionhandling;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ExceptionInfo handleException(CustomException customException) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setInfo(customException.getMessage());
        return exceptionInfo;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ExceptionInfo handleException(MethodArgumentNotValidException exception) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setInfo(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
        return exceptionInfo;
    }
}
