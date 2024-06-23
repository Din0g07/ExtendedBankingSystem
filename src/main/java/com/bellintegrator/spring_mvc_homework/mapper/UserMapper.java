package com.bellintegrator.spring_mvc_homework.mapper;

import com.bellintegrator.spring_mvc_homework.model.dto.UserDTORq;
import com.bellintegrator.spring_mvc_homework.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTORq map(User user);

    User map(UserDTORq userDTORq);
}
