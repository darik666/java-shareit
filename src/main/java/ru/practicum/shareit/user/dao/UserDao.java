package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс хранилища пользователей
 */
@Component
public interface UserDao {
    List<User> getUsers();

    User getUserById(Integer id);

    User postUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);
}