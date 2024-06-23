package com.bellintegrator.spring_mvc_homework.controller;

import com.bellintegrator.spring_mvc_homework.DataGenerator;
import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORq;
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

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataGenerator dataGenerator;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String USER_URL = "/users";

    private final String PERSONAL_ACCOUNT_URL = "/personalAccount/{userId}";
    private UserDTORq userDTORq;

    @BeforeAll
    public void prepareDB() {
        User user = dataGenerator.addUserToDataBase(DataGenerator.FAKE_USER_ID);
        dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID1, user, BankAccountType.DEPOSIT);
        BankAccount account = dataGenerator.addBankAccountToDataBase(DataGenerator.FAKE_ACCOUNT_ID2, user, BankAccountType.DEBIT);
        dataGenerator.addCardToDataBase(DataGenerator.FAKE_CARD_ID, account, CardType.DEBIT);
    }

    @BeforeEach
    public void prepareData() {
        userDTORq = new UserDTORq();
        userDTORq.setName(DataGenerator.FAKE_NAME);
        userDTORq.setSurname(DataGenerator.FAKE_SURNAME);
        userDTORq.setDate(LocalDate.parse("1998-12-26"));
        userDTORq.setPassportNumber(DataGenerator.FAKE_PASSPORT);
    }

    @AfterAll
    public void clearDB() {
        dataGenerator.clearUserWithAllLinks(DataGenerator.FAKE_USER_ID);
    }
    @Test
    void shouldAddNewUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("id")));
    }

    @Test
    void shouldReturnErrorNotNull() throws Exception {
        userDTORq.setDate(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("error")));
    }

    @Test
    void shouldReturnErrorSize() throws Exception {
        userDTORq.setName("Ибн-Аль Нах Даздраперма (Да здравствует Первое Мая)");

        this.mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("error")));
    }

    @Test
    void shouldReturnErrorPattern() throws Exception {
        userDTORq.setPassportNumber("abc");

        this.mockMvc.perform(MockMvcRequestBuilders.post(USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTORq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("error")));
    }

    @Test
    void shouldGetPersonalAccount() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(PERSONAL_ACCOUNT_URL
                        .replace("{userId}", String.valueOf(DataGenerator.FAKE_USER_ID))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("name")));
    }
}
