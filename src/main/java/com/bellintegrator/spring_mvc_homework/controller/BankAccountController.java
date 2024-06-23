package com.bellintegrator.spring_mvc_homework.controller;

import com.bellintegrator.spring_mvc_homework.model.dto.BalanceUpdateDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.BalanceUpdateDTORs;
import com.bellintegrator.spring_mvc_homework.model.dto.BankAccountDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.BankAccountDTORs;
import com.bellintegrator.spring_mvc_homework.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class BankAccountController {
    @Autowired
    private BankAccountService bankAccountService;
    @PostMapping("/account")
    public BankAccountDTORs openNewDepositBankAccount(@RequestBody @Valid BankAccountDTORq bankAccountDTORq) {
        return bankAccountService.openNewDepositBankAccount(bankAccountDTORq);
    }

    @PostMapping("/transfer")
    public BalanceUpdateDTORs transfer(@RequestBody @Valid BalanceUpdateDTORq balanceUpdateDTORq) {
        return bankAccountService.transfer(balanceUpdateDTORq);
    }
}
