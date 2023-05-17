package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Интерфейс сервисного класса пользователей
 */
@Component
public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(Integer id);

    UserDto postUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    void deleteUser(Integer id);
}