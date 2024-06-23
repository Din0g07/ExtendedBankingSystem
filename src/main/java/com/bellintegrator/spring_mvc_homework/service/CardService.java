package com.bellintegrator.spring_mvc_homework.service;

import com.bellintegrator.spring_mvc_homework.common.NumberGenerator;
import com.bellintegrator.spring_mvc_homework.common.NumberTypeForGenerator;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.CustomException;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.ExceptionTypes;
import com.bellintegrator.spring_mvc_homework.model.dto.CardDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.CardDTORs;
import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.Card;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardStatus;
import com.bellintegrator.spring_mvc_homework.model.repository.BankAccountRepository;
import com.bellintegrator.spring_mvc_homework.model.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CardRepository cardRepository;

    @Transactional
    public CardDTORs openNewCard(CardDTORq cardDTORq) {
        BankAccount bankAccount;
        if (cardDTORq.getAccountId() != null) {
            bankAccount = bankAccountRepository.findById(cardDTORq.getAccountId())
                    .orElseThrow(() -> new CustomException(ExceptionTypes.ACCOUNT_NOT_FOUND));
            if(!bankAccount.getType().equals(cardDTORq.getCardType().name())) {
                throw new CustomException(ExceptionTypes.TYPES_MISMATCH);
            }
        } else {
            bankAccount = bankAccountService.saveBankAccount(cardDTORq.getCardType().name(), cardDTORq.getUserId());
        }

        if((Objects.equals(bankAccount.getType(), BankAccountType.CREDIT.name()))
        && (bankAccount.getCards() != null && !bankAccount.getCards().isEmpty())) {
            throw new CustomException(ExceptionTypes.CREDIT_CARD_LIMIT);
        }

        Card card = new Card();
        card.setId(UUID.randomUUID());
        card.setType(cardDTORq.getCardType().name());
        card.setPaymentSystem(cardDTORq.getPaymentSystem());
        card.setUser(bankAccount.getUser());
        card.setBankAccount(bankAccount);
        card.setBalance(bankAccount.getBalance());
        card.setStatus(CardStatus.ACTIVE.name());
        card.setNumber(NumberGenerator.generateAccountNumber(NumberTypeForGenerator.CARD));

        cardRepository.save(card);
        return new CardDTORs(card.getNumber());
    }

    public void closeCard(BigDecimal cardNumber ) {
        changeStatus(cardNumber, CardStatus.CLOSED);
    }

    public void blockCard(BigDecimal cardNumber ) {
        changeStatus(cardNumber, CardStatus.BLOCKED);
    }

    private void changeStatus(BigDecimal cardNumber, CardStatus status) {
        Card card = cardRepository.findByNumber(cardNumber)
                .orElseThrow(() -> new CustomException(ExceptionTypes.CARD_NOT_FOUND));
        card.setStatus(status.name());
        cardRepository.save(card);
    }
}
