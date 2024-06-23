package com.bellintegrator.spring_mvc_homework.service;


import com.bellintegrator.spring_mvc_homework.DataGenerator;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.CustomException;
import com.bellintegrator.spring_mvc_homework.model.dto.BalanceUpdateDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.BalanceUpdateDTORs;
import com.bellintegrator.spring_mvc_homework.model.dto.BankAccountDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.BankAccountDTORs;
import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
import com.bellintegrator.spring_mvc_homework.model.enums.Operation;
import com.bellintegrator.spring_mvc_homework.model.repository.BankAccountRepository;
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

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankAccountServiceTest {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private DataGenerator dataGenerator;

    private BankAccountDTORq bankAccountDTORq;
    private BalanceUpdateDTORq balanceUpdateDTORq;

    @BeforeAll
    public void prepareDB() {
        User user = dataGenerator.addUserToDataBase(DataGenerator.FAKE_USER_ID);
        dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID1, user, BankAccountType.DEPOSIT);
        BankAccount account = dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID2, user, BankAccountType.DEBIT);
        dataGenerator.addCardToDataBase(DataGenerator.FAKE_CARD_ID, account, CardType.DEBIT);
    }

    @BeforeEach
    public void prepareData() {
        bankAccountDTORq = new BankAccountDTORq();
        bankAccountDTORq.setUserId(DataGenerator.FAKE_USER_ID);
        balanceUpdateDTORq = new BalanceUpdateDTORq();
        balanceUpdateDTORq.setBankAccountId(DataGenerator.FAKE_ACCOUNT_ID1);
        balanceUpdateDTORq.setSum(BigDecimal.valueOf(1000));
        balanceUpdateDTORq.setOperation(Operation.DEPOSIT);
    }

    @AfterAll
    public void clearDB() {
        dataGenerator.clearUserWithAllLinks(DataGenerator.FAKE_USER_ID);
    }

    @Test
    @Transactional
    public void shouldOpenNewAccount() {
        BankAccountDTORs dto = bankAccountService.openNewDepositBankAccount(bankAccountDTORq);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getNumber());
        BankAccount bankAccount = bankAccountRepository.findByNumber(dto.getNumber()).get();
        Assertions.assertEquals(bankAccountDTORq.getUserId(), bankAccount.getUser().getId());
        Assertions.assertEquals(BankAccountType.DEPOSIT.name(), bankAccount.getType());
    }

    @Test
    @Transactional
    public void shouldDepositMoneyToAccount() {
        BalanceUpdateDTORs dto = bankAccountService.transfer(balanceUpdateDTORq);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getBalance());
        BankAccount bankAccount = bankAccountRepository.findById(DataGenerator.FAKE_ACCOUNT_ID1).get();
        Assertions.assertEquals(balanceUpdateDTORq.getSum(), bankAccount.getBalance().setScale(0));
    }

    @Test
    @Transactional
    public void shouldDepositAndWithdrawMoneyFromCard() {
        balanceUpdateDTORq.setBankAccountId(null);
        balanceUpdateDTORq.setCardId(DataGenerator.FAKE_CARD_ID);
        bankAccountService.transfer(balanceUpdateDTORq);
        balanceUpdateDTORq.setOperation(Operation.WITHDRAWAL);

        BalanceUpdateDTORs dto = bankAccountService.transfer(balanceUpdateDTORq);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getBalance());
        BankAccount bankAccount = bankAccountRepository.findById(DataGenerator.FAKE_ACCOUNT_ID1).get();
        Assertions.assertEquals(BigDecimal.ZERO, bankAccount.getBalance().setScale(0));
    }

    @Test
    @Transactional
    public void shouldThrowExceptionWhileWithdrawMoney() {
        balanceUpdateDTORq.setSum(BigDecimal.valueOf(10000000));
        balanceUpdateDTORq.setOperation(Operation.WITHDRAWAL);
        Assertions.assertThrows(CustomException.class, () -> bankAccountService.transfer(balanceUpdateDTORq));
    }

}
