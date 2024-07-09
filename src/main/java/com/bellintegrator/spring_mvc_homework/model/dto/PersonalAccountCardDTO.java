package com.bellintegrator.spring_mvc_homework.model.dto;

import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class PersonalAccountCardDTO {
    private BigDecimal number;
    private String type;
    private String paymentSystem;
    private String status;
}
