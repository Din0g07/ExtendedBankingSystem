package com.bellintegrator.spring_mvc_homework.controller;

import com.bellintegrator.spring_mvc_homework.model.dto.CardDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.CardDTORs;
import com.bellintegrator.spring_mvc_homework.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Validated
public class CardController {
    @Autowired
    private CardService cardService;
    @PostMapping("/card")
    public CardDTORs openNewCard(@RequestBody @Valid CardDTORq cardDTORq) {
        return cardService.openNewCard(cardDTORq);
    }

    @PutMapping("/card/{cardNumber}/close")
    public void closeCard(@PathVariable BigDecimal cardNumber) {
        cardService.closeCard(cardNumber);
    }

    @PutMapping("/card/{cardNumber}/block")
    public void blockCard(@PathVariable BigDecimal cardNumber) {
        cardService.blockCard(cardNumber);
    }
}
