package com.bellintegrator.spring_mvc_homework.model.repository;

import com.bellintegrator.spring_mvc_homework.model.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<Card> findByNumber(BigDecimal cardNumber);
}
