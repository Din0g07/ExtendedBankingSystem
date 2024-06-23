package com.bellintegrator.spring_mvc_homework.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BankAccountDTORq {
    @NotNull(message = "ИД пользователя не должно отсутствовать")
    private UUID userId;
}
