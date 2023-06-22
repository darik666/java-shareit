package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * DTO модель пользователей
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;

    @Email(groups = {UserDto.OnCreate.class})
    @NotBlank(groups = {UserDto.OnCreate.class})
    private String email;

    public interface OnCreate {
    }
}