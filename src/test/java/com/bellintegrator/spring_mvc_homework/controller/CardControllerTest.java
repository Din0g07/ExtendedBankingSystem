package com.bellintegrator.spring_mvc_homework.controller;

import com.bellintegrator.spring_mvc_homework.DataGenerator;
import com.bellintegrator.spring_mvc_homework.model.dto.CardDTORq;
import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardType;
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

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataGenerator dataGenerator;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String CARD_URL = "/card";
    private final String BLOCK_CARD_URL = "/card/{cardNumber}/block";
    private final String CLOSE_CARD_URL = "/card/{cardNumber}/close";
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
    void shouldAddNewCard() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(CARD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("number")));
    }

    @Test
    void shouldReturnErrorNotNull() throws Exception {
        cardDTORq.setUserId(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(CARD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("error")));
    }

    @Test
    void shouldReturnErrorSize() throws Exception {
        cardDTORq.setPaymentSystem("МирMasterMaestroVisaShengenCard Version 2.03.122312");

        this.mockMvc.perform(MockMvcRequestBuilders.post(CARD_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("error")));
    }

    @Test
    void shouldCloseCard() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(CLOSE_CARD_URL
                        .replace("{cardNumber}", String.valueOf(DataGenerator.FAKE_CARD_NUMBER))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldBlockCard() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(BLOCK_CARD_URL
                        .replace("{cardNumber}", String.valueOf(DataGenerator.FAKE_CARD_NUMBER))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}