package com.bellintegrator.spring_mvc_homework.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class BalanceUpdateDTORs {
    private BigDecimal balance;
}
