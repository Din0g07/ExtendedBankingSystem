package com.bellintegrator.spring_mvc_homework.exceptionhandling;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    public CustomException(ExceptionTypes type) {
        super(type.getMessage());
    }
}
