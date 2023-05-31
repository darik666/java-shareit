package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * DTO модель пользователей
 */
@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    @Email
    private String email;
}