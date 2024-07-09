package com.bellintegrator.spring_mvc_homework.service;

import com.bellintegrator.spring_mvc_homework.DataGenerator;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.CustomException;
import com.bellintegrator.spring_mvc_homework.model.dto.PersonalAccountDTO;
import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORs;
import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardStatus;
import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
import com.bellintegrator.spring_mvc_homework.model.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataGenerator dataGenerator;
    private UserDTORq userDTORq;

    @BeforeAll
    public void prepareData() {
        userDTORq = new UserDTORq();
        userDTORq.setName(DataGenerator.FAKE_NAME);
        userDTORq.setSurname(DataGenerator.FAKE_SURNAME);
        userDTORq.setDate(DataGenerator.FAKE_DATE);
        userDTORq.setPassportNumber(DataGenerator.FAKE_PASSPORT);

        User user = dataGenerator.addUserToDataBase(DataGenerator.FAKE_USER_ID);
        dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID1, user, BankAccountType.DEPOSIT);
        BankAccount account = dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID2, user, BankAccountType.DEBIT);
        dataGenerator.addCardToDataBase(DataGenerator.FAKE_CARD_ID, account, CardType.DEBIT);
    }

    @AfterAll
    public void clearDB() {
        dataGenerator.clearUserWithAllLinks(DataGenerator.FAKE_USER_ID);
    }

    @Test
    @Transactional
    public void shouldAddNewUser() {
        UserDTORs dto = userService.addNewUser(userDTORq);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getId());
        User user = userRepository.findById(dto.getId()).get();
        Assertions.assertEquals(userDTORq.getName(), user.getName());
        Assertions.assertEquals(userDTORq.getSurname(), user.getSurname());
        Assertions.assertEquals(userDTORq.getPassportNumber(), user.getPassportNumber());
        Assertions.assertEquals(userDTORq.getDate(), user.getDate());
        Assertions.assertNull(user.getBankAccounts());
        Assertions.assertNull(user.getCards());
    }

//    @Test
//    @Transactional
//    public void shouldThrowValidationExceptionWhileAddingNewUser() {
//        userDTORq.setName(null);
//        Assertions.assertThrows(MethodArgumentNotValidException.class, () -> {
//            userService.addNewUser(userDTORq);});
//    }

    @Test
    @Transactional
    public void shouldGetPersonalAccount() {
        PersonalAccountDTO dto = userService.showPersonalAccount(DataGenerator.FAKE_USER_ID);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(DataGenerator.FAKE_NAME, dto.getName());
        Assertions.assertEquals(DataGenerator.FAKE_SURNAME, dto.getSurname());
        Assertions.assertNotNull(dto.getAccounts());
        Assertions.assertTrue(dto.getAccounts().stream()
                .anyMatch(acc -> DataGenerator.FAKE_ACCOUNT_NUMBER.equals(acc.getNumber())));
        Assertions.assertTrue(dto.getAccounts().stream()
                .anyMatch(acc -> BankAccountType.DEPOSIT.name().equals(acc.getType())));
        Assertions.assertTrue(dto.getAccounts().stream()
                .filter(acc -> !acc.getCards().isEmpty())
                .anyMatch(acc -> DataGenerator.FAKE_CARD_NUMBER.equals(acc.getCards().get(0).getNumber())));
        Assertions.assertTrue(dto.getAccounts().stream()
                .filter(acc -> !acc.getCards().isEmpty())
                .anyMatch(acc -> CardType.DEBIT.name().equals(acc.getCards().get(0).getType())));
        Assertions.assertTrue(dto.getAccounts().stream()
                .filter(acc -> !acc.getCards().isEmpty())
                .anyMatch(acc -> DataGenerator.FAKE_PAYMENT_SYSTEM.equals(acc.getCards().get(0).getPaymentSystem())));
        Assertions.assertTrue(dto.getAccounts().stream()
                .filter(acc -> !acc.getCards().isEmpty())
                .anyMatch(acc -> CardStatus.ACTIVE.name().equals(acc.getCards().get(0).getStatus())));
    }

    @Test
    @Transactional
    public void shouldGThrowExceptionNonExistingUser() {
        Assertions.assertThrows(CustomException.class, () -> userService.showPersonalAccount(UUID.randomUUID()));
    }
}
