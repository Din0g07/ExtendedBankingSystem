package com.bellintegrator.spring_mvc_homework.model.entities;

import com.bellintegrator.spring_mvc_homework.model.enums.BankAccountType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bank_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankAccount {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "number")
    private BigDecimal number;

    @Column(name = "type")
    private String type;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankAccount")
    private List<Card> cards;
}
