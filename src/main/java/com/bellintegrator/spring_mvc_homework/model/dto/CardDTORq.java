package com.bellintegrator.spring_mvc_homework.model.dto;

import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CardDTORq {
    @NotNull(message = "ИД пользователя не должно отсутствовать")
    private UUID userId;
    @Nullable
    private UUID accountId;
    @NotNull(message = "Тип карты не должно отсутствовать")
    private CardType cardType;
    @NotNull(message = "Платежная система не должно отсутствовать")
    @Size(max = 50, message = "Платежная система должна содержать не более 50 символов")
    private String paymentSystem;
}
