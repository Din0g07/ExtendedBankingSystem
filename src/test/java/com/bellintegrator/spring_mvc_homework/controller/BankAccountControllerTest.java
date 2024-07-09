package com.bellintegrator.spring_mvc_homework.controller;

import com.bellintegrator.spring_mvc_homework.DataGenerator;
import com.bellintegrator.spring_mvc_homework.model.dto.BalanceUpdateDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.BankAccountDTORq;
import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
import com.bellintegrator.spring_mvc_homework.model.enums.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataGenerator dataGenerator;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String ACCOUNT_URL = "/account";
    private final String TRANSFER_URL = "/transfer";
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
    void shouldAddNewBankAccount() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(ACCOUNT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bankAccountDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("number")));
    }

    @Test
    void shouldReturnErrorNotNull() throws Exception {
        bankAccountDTORq.setUserId(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ACCOUNT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bankAccountDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("error")));
    }

    @Test
    void shouldTransferMoney() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(TRANSFER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(balanceUpdateDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("balance")));
    }
}
