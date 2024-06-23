package com.bellintegrator.spring_mvc_homework.model.dto;

import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
public class PersonalAccountBankAccountDTO {
    private BigDecimal number;

    private String type;

    private BigDecimal balance;

    private List<PersonalAccountCardDTO> cards;
}
