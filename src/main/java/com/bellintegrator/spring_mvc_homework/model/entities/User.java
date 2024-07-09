package com.bellintegrator.spring_mvc_homework.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "passport_number")
    private String passportNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<BankAccount> bankAccounts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Card> cards;

//    public <T> User(UUID uuidTest1, String name, String surname, LocalDate date, String passportNumber, List<T> ts) {
//    }
}
