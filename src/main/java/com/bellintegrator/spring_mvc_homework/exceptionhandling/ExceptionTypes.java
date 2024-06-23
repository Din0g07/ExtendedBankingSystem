package com.bellintegrator.spring_mvc_homework.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionTypes {

    USER_NOT_FOUND("Пользователь не найден"),
    ACCOUNT_NOT_FOUND("Счет не найден"),
    CARD_NOT_FOUND("Карта не найден"),
    NOT_ENOUGH_BALANCE("Недостаточно средств"),
    OPERATION_NOT_SUPPORTED("Некорректная операция"),
    NEED_INFO("Недостаточно данных для провеедения операции"),
    OPERATION_SUPPORTED_ONLY_WITH_DEPOSIT("Операция доступна только с депозитным счетом"),
    OPERATION_SUPPORTED_ONLY_WITH_ACTIVE("Операция доступна только с активной картой"),
    TYPES_MISMATCH("Тип счета не совпадает с типом карты"),
    CREDIT_CARD_LIMIT("К кредитному счету нельзя привязать больше чем одну карту")
    ;

    private final String message;
}
