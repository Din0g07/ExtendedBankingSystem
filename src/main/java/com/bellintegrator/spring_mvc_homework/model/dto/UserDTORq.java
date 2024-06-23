package com.bellintegrator.spring_mvc_homework.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTORq {
    @NotNull(message = "Имя не должно быть пустым")
    @Size(max = 50, message = "Имя должно содержать не более 50 символов")
    private String name;

    @NotNull(message = "Фамилия не должна быть пустой")
    @Size(max = 100, message = "Фамилия должна содержать не более 100 символов")
    private String surname;

    @NotNull(message = "Дата рождения не должна быть пустой")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @NotNull(message = "Паспорт не должен быть пустым")//еще проверку
    @Pattern(regexp = "\\d{10}", message = "Паспорт должен содержать 10 цифр")
    private String passportNumber;
}
