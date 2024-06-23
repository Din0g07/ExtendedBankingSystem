package com.bellintegrator.spring_mvc_homework.service;

import com.bellintegrator.spring_mvc_homework.common.Constants;
import com.bellintegrator.spring_mvc_homework.common.NumberGenerator;
import com.bellintegrator.spring_mvc_homework.common.NumberTypeForGenerator;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.CustomException;
import com.bellintegrator.spring_mvc_homework.exceptionhandling.ExceptionTypes;
import com.bellintegrator.spring_mvc_homework.model.dto.BalanceUpdateDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.BalanceUpdateDTORs;
import com.bellintegrator.spring_mvc_homework.model.dto.BankAccountDTORq;
import com.bellintegrator.spring_mvc_homework.model.dto.BankAccountDTORs;
import com.bellintegrator.spring_mvc_homework.model.entities.BankAccount;
import com.bellintegrator.spring_mvc_homework.model.entities.Card;
import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import com.bellintegrator.spring_mvc_homework.model.enums.CardStatus;
import com.bellintegrator.spring_mvc_homework.model.repository.BankAccountRepository;
import com.bellintegrator.spring_mvc_homework.model.repository.CardRepository;
import com.bellintegrator.spring_mvc_homework.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class BankAccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CardRepository cardRepository;
    public BankAccountDTORs openNewDepositBankAccount(BankAccountDTORq bankAccountDTORq) {
        BankAccount bankAccount = saveBankAccount(BankAccountType.DEPOSIT.name(), bankAccountDTORq.getUserId());
        return new BankAccountDTORs(bankAccount.getNumber());
    }

    public BankAccount saveBankAccount(String type, UUID userId) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.randomUUID());
        bankAccount.setNumber(NumberGenerator.generateAccountNumber(NumberTypeForGenerator.BANK_ACCOUNT));
        bankAccount.setType(type);

        // открываем счет со стандартным лимитом
        if (Objects.equals(type, BankAccountType.CREDIT.name())) {
            bankAccount.setBalance(Constants.BASIC_CREDIT_LIMIT);
        }

        bankAccount.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionTypes.USER_NOT_FOUND)));
        bankAccountRepository.save(bankAccount);
        return bankAccount;
    }

    public BalanceUpdateDTORs transfer(BalanceUpdateDTORq balanceUpdateDTORq) {
        if(balanceUpdateDTORq.getBankAccountId() != null) {
            return new BalanceUpdateDTORs(transferBankAccount(balanceUpdateDTORq));
        } else if (balanceUpdateDTORq.getCardId() != null) {
            return new BalanceUpdateDTORs(transferCard(balanceUpdateDTORq));
        } else {
            throw new CustomException(ExceptionTypes.NEED_INFO);
        }
    }

    private BigDecimal transferCard(BalanceUpdateDTORq balanceUpdateDTORq) {
        Card card = cardRepository.findById(balanceUpdateDTORq.getCardId())
                .orElseThrow(() -> new CustomException(ExceptionTypes.CARD_NOT_FOUND));
        if (!Objects.equals(card.getStatus(), CardStatus.ACTIVE.name())) {
            throw new CustomException(ExceptionTypes.OPERATION_SUPPORTED_ONLY_WITH_ACTIVE);
        }

        switch (balanceUpdateDTORq.getOperation()) {
            case DEPOSIT -> card.setBalance(card.getBalance().add(balanceUpdateDTORq.getSum()));
            case WITHDRAWAL-> {
                if (card.getBalance().compareTo(balanceUpdateDTORq.getSum()) < 0) {
                    throw new CustomException(ExceptionTypes.NOT_ENOUGH_BALANCE);
                }
                card.setBalance(card.getBalance().subtract(balanceUpdateDTORq.getSum()));
            }
            default -> throw new CustomException(ExceptionTypes.OPERATION_NOT_SUPPORTED);
        }
        card.getBankAccount().setBalance(card.getBalance());
        card.getBankAccount().getCards().forEach((cardObj) -> cardObj.setBalance(card.getBalance()));
        cardRepository.save(card);
        return card.getBalance();
    }

    private BigDecimal transferBankAccount(BalanceUpdateDTORq balanceUpdateDTORq)  {
        BankAccount bankAccount = bankAccountRepository.findById(balanceUpdateDTORq.getBankAccountId())
                .orElseThrow(() -> new CustomException(ExceptionTypes.ACCOUNT_NOT_FOUND));
        if (!Objects.equals(bankAccount.getType(), BankAccountType.DEPOSIT.name())) {
            throw new CustomException(ExceptionTypes.OPERATION_SUPPORTED_ONLY_WITH_DEPOSIT);
        }

        switch (balanceUpdateDTORq.getOperation()) {
            case DEPOSIT -> bankAccount.setBalance(bankAccount.getBalance().add(balanceUpdateDTORq.getSum()));
            case WITHDRAWAL-> {
                if (bankAccount.getBalance().compareTo(balanceUpdateDTORq.getSum()) < 0) {
                    throw new CustomException(ExceptionTypes.NOT_ENOUGH_BALANCE);
                }
                bankAccount.setBalance(bankAccount.getBalance().subtract(balanceUpdateDTORq.getSum()));
            }
            default -> throw new CustomException(ExceptionTypes.OPERATION_NOT_SUPPORTED);
        }
        bankAccountRepository.save(bankAccount);
        return bankAccount.getBalance();
    }


}
