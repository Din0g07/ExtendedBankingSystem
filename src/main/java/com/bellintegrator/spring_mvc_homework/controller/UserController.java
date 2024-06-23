package com.bellintegrator.spring_mvc_homework.controller;

import com.bellintegrator.spring_mvc_homework.model.dto.PersonalAccountDTO;
import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORs;
import com.bellintegrator.spring_mvc_homework.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public UserDTORs registration(@RequestBody @Valid UserDTORq userDTORq) {
        return userService.addNewUser(userDTORq);
    }

    @GetMapping("/personalAccount/{userId}")
    public PersonalAccountDTO showPersonalAccount(@PathVariable UUID userId) {
        return userService.showPersonalAccount(userId);
    }
}
