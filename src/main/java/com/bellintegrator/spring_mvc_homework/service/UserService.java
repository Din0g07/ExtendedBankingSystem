package com.bellintegrator.spring_mvc_homework.service;

import com.bellintegrator.spring_mvc_homework.exceptionhandling.CustomException;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.ExceptionTypes;
import com.bellintegrator.spring_mvc_homework.mapper.UserMapper;
import com.bellintegrator.spring_mvc_homework.model.dto.PersonalAccountBankAccountDTO;
import com.bellintegrator.spring_mvc_homework.model.dto.PersonalAccountCardDTO;
import com.bellintegrator.spring_mvc_homework.model.dto.PersonalAccountDTO;
import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORs;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import com.bellintegrator.spring_mvc_homework.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    public UserDTORs addNewUser(UserDTORq userDTORq) {
        User user = userMapper.map(userDTORq);
        user.setId(UUID.randomUUID());

        userRepository.save(user);
        return new UserDTORs(user.getId());
    }

    public PersonalAccountDTO showPersonalAccount(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionTypes.USER_NOT_FOUND));

        PersonalAccountDTO personalAccountDTO = new PersonalAccountDTO();

        personalAccountDTO.setName(user.getName());//ну или через маппер
        personalAccountDTO.setSurname(user.getSurname());
        personalAccountDTO.setAccounts(user.getBankAccounts()
                .stream().map((bankAccount) -> PersonalAccountBankAccountDTO.builder()
                .balance(bankAccount.getBalance())
                .number(bankAccount.getNumber())
                .type(bankAccount.getType())
                //по желанию можно отфильтровывать карты по типу (не показывать заблокированные или закрытые)
                .cards(bankAccount.getCards().stream().map((card -> PersonalAccountCardDTO.builder()
                        .type(card.getType())
                        .paymentSystem(card.getPaymentSystem())
                        .status(card.getStatus())
                        .number(card.getNumber())
                        .build())).collect(Collectors.toList()))
                .build()).collect(Collectors.toList()));

        return personalAccountDTO;
    }

}
