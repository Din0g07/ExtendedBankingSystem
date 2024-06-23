package com.bellintegrator.spring_mvc_homework.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PersonalAccountDTO {
    private String name;

    private String surname;

    private List<PersonalAccountBankAccountDTO> accounts;
}
