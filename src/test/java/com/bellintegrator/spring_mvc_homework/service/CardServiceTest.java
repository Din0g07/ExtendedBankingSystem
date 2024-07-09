package com.bellintegrator.spring_mvc_homework.service;

import com.bellintegrator.spring_mvc_homework.DataGenerator;
import com.bellintegrator.spring_mvc_homework.common.Constants;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.CustomException;
import com.bellintegrator.spring_mvc_homework.model.dto.CardDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.CardDTORs;
import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.Card;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardStatus;
import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
import com.bellintegrator.spring_mvc_homework.model.repository.CardRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardServiceTest {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardService cardService;
    @Autowired
    private DataGenerator dataGenerator;

    private CardDTORq cardDTORq;

    @BeforeAll
    public void prepareDB() {
        User user = dataGenerator.addUserToDataBase(DataGenerator.FAKE_USER_ID);
        dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID1, user, BankAccountType.DEBIT);
        BankAccount account = dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID2, user, BankAccountType.DEBIT);
        dataGenerator.addCardToDataBase(DataGenerator.FAKE_CARD_ID, account, CardType.DEBIT);
    }

    @BeforeEach
    public void prepareData() {
        cardDTORq = new CardDTORq();
        cardDTORq.setUserId(DataGenerator.FAKE_USER_ID);
        cardDTORq.setAccountId(DataGenerator.FAKE_ACCOUNT_ID1);
        cardDTORq.setCardType(CardType.DEBIT);
        cardDTORq.setPaymentSystem(DataGenerator.FAKE_PAYMENT_SYSTEM);
    }

    @AfterAll
    public void clearDB() {
        dataGenerator.clearUserWithAllLinks(DataGenerator.FAKE_USER_ID);
    }

    @Test
    @Transactional
    public void shouldAddDebitNewCardToAnExistingAccount() {
        CardDTORs dto = cardService.openNewCard(cardDTORq);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getNumber());
        Card card = cardRepository.findByNumber(dto.getNumber()).get();
        Assertions.assertEquals(cardDTORq.getUserId(), card.getUser().getId());
        Assertions.assertEquals(cardDTORq.getAccountId(), card.getBankAccount().getId());
        Assertions.assertEquals(cardDTORq.getCardType().name(), card.getType());
        Assertions.assertEquals(cardDTORq.getPaymentSystem(), card.getPaymentSystem());
        Assertions.assertEquals(CardStatus.ACTIVE.name(), card.getStatus());
    }

    @Test
    @Transactional
    public void shouldAddCreditNewCardWithNewAccount() {
        cardDTORq.setCardType(CardType.CREDIT);
        cardDTORq.setAccountId(null);

        CardDTORs dto = cardService.openNewCard(cardDTORq);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getNumber());
        Card card = cardRepository.findByNumber(dto.getNumber()).get();
        Assertions.assertEquals(cardDTORq.getUserId(), card.getUser().getId());
        Assertions.assertNotNull(card.getBankAccount().getId());
        Assertions.assertEquals(cardDTORq.getCardType().name(), card.getType());
        Assertions.assertEquals(cardDTORq.getPaymentSystem(), card.getPaymentSystem());
        Assertions.assertEquals(Constants.BASIC_CREDIT_LIMIT, card.getBalance());
        Assertions.assertEquals(CardStatus.ACTIVE.name(), card.getStatus());
    }

    @Test
    @Transactional
    public void shouldThrowExceptionNonExistingAccount() {
        cardDTORq.setAccountId(UUID.randomUUID());
        Assertions.assertThrows(CustomException.class, () -> cardService.openNewCard(cardDTORq));
    }

    @Test
    @Transactional
    public void shouldThrowExceptionTypesMismatch() {
        cardDTORq.setCardType(CardType.CREDIT);
        Assertions.assertThrows(CustomException.class, () -> cardService.openNewCard(cardDTORq));
    }

    @Test
    @Transactional
    public void shouldBlockCard() {
        cardService.blockCard(DataGenerator.FAKE_CARD_NUMBER);
        Card card = cardRepository.findById(DataGenerator.FAKE_CARD_ID).get();
        Assertions.assertEquals(CardStatus.BLOCKED.name(), card.getStatus());
    }

    @Test
    @Transactional
    public void shouldCloseCard() {
        cardService.closeCard(DataGenerator.FAKE_CARD_NUMBER);
        Card card = cardRepository.findById(DataGenerator.FAKE_CARD_ID).get();
        Assertions.assertEquals(CardStatus.CLOSED.name(), card.getStatus());
    }

    @Test
    @Transactional
    public void shouldThrowExceptionNonExistingCard() {
        Assertions.assertThrows(CustomException.class, () -> cardService.blockCard(new BigDecimal(123)));
    }
}
