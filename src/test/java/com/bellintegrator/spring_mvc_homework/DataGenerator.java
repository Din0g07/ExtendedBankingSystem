package com.bellintegrator.spring_mvc_homework;

import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.Card;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardStatus;
import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
import com.bellintegrator.spring_mvc_homework.model.repository.BankAccountRepository;
import com.bellintegrator.spring_mvc_homework.model.repository.CardRepository;
import com.bellintegrator.spring_mvc_homework.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class DataGenerator {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CardRepository cardRepository;

    public static final UUID FAKE_USER_ID = UUID.randomUUID();
    public static final UUID FAKE_ACCOUNT_ID1 = UUID.randomUUID();
    public static final UUID FAKE_ACCOUNT_ID2 = UUID.randomUUID();
    public static final UUID FAKE_CARD_ID = UUID.randomUUID();
    public static final String FAKE_NAME = "Имя";
    public static final String FAKE_SURNAME = "Фамилия";
    public static final LocalDate FAKE_DATE = LocalDate.now();
    public static final String FAKE_PASSPORT = "1234567890";
    public static final BigDecimal FAKE_ACCOUNT_NUMBER = new BigDecimal("12345678901234567890");
    public static final BigDecimal FAKE_CARD_NUMBER = new BigDecimal("1234567890123456");
    public static final String FAKE_PAYMENT_SYSTEM = "Мир";

    public User addUserToDataBase(UUID id) {
        User user = new User();
        user.setId(id);
        user.setName(FAKE_NAME);
        user.setSurname(FAKE_SURNAME);
        user.setDate(FAKE_DATE);
        user.setPassportNumber(FAKE_PASSPORT);
        return userRepository.save(user);
    }

    public BankAccount addBankAccountToDataBase(UUID id, User user, BankAccountType type) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        bankAccount.setUser(user);
        bankAccount.setNumber(FAKE_ACCOUNT_NUMBER);
        bankAccount.setType(type.name());
        bankAccount.setBalance(BigDecimal.ZERO);
        return bankAccountRepository.save(bankAccount);
    }

    public Card addCardToDataBase(UUID id, BankAccount account, CardType type) {
        Card card = new Card();
        card.setId(id);
        card.setBankAccount(account);
        card.setUser(account.getUser());
        card.setNumber(FAKE_CARD_NUMBER);
        card.setType(type.name());
        card.setStatus(CardStatus.ACTIVE.name());
        card.setPaymentSystem(FAKE_PAYMENT_SYSTEM);
        card.setBalance(BigDecimal.ZERO);
        return cardRepository.save(card);
    }

    public void clearUserWithAllLinks(UUID id) {
        userRepository.deleteById(id);
    }
}
