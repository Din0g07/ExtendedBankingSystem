package com.bellintegrator.spring_mvc_homework.model.dto;

import com.bellintegrator.spring_mvc_homework.model.enums.Operation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class BalanceUpdateDTORq {
    @Nullable
    UUID bankAccountId;
    @Nullable
    UUID cardId;
    @NotNull(message = "Сумма не должна отсутствовать")
    BigDecimal sum;
    @NotNull(message = "Операция не должна отсутствовать")
    Operation operation;
}
