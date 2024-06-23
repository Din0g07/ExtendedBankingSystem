package com.bellintegrator.spring_mvc_homework.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NumberTypeForGenerator {
    CARD(16),
    BANK_ACCOUNT(20);

    private final int length;
}
