package com.bellintegrator.spring_mvc_homework.model.repository;

import com.bellintegrator.spring_mvc_homework.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
