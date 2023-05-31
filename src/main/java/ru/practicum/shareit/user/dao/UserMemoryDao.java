package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Хранилище пользователей в памяти
 */
@Slf4j
@Primary
@Repository
public class UserMemoryDao implements UserDao {
    private HashMap<Integer, User> userList = new HashMap<>();
    private Integer id = 0;

    /**
     * Получение пользователей
     */
    @Override
    public List<User> getUsers() {
        log.info("Получение списка пользователей");
        return userList.values().stream()
                .collect(Collectors.toList());
    }

    /**
     * Получение пользователя по id
     */
    @Override
    public User getUserById(Integer id) {
        log.info("Получение пользователя с id = {}", id);
        Optional<User> user = Optional.ofNullable(userList.get(id));
        return user.orElseThrow(() -> new UserNotFoundException(
                String.format("Пользователь с id %d не найден", id)));
    }

    /**
     * Создание пользователя
     */
    @Override
    public User postUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.warn("Необходим email");
            throw new IllegalArgumentException("Необходим email");
        }
        boolean emailExists = userList.values().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (emailExists) {
            log.warn("Данный email: {} уже существует: ", user.getEmail());
            throw new EmailExistsException("Данный email уже существует: " + user.getEmail());
        }
        user.setId(++id);
        userList.put(user.getId(), user);
        log.info("Создание пользователя: {}", user);
        return user;
    }

    /**
     * Обновление пользователя
     */
    @Override
    public User updateUser(User user) {
        Optional<User> testUser = Optional.ofNullable(userList.get(user.getId()));
        if (testUser.isEmpty()) {
            log.warn("Пользователь с данным id не найден");
            throw new UserNotFoundException("Пользователь с данным id не найден");
        }
        User existingUser = testUser.get();
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            String newEmail = user.getEmail();
            boolean emailExistsInOtherUsers = userList.values().stream()
                    .filter(u -> !u.getId().equals(existingUser.getId()))
                    .anyMatch(u -> u.getEmail().equals(newEmail));
            if (emailExistsInOtherUsers) {
                log.warn("Данный email: {} уже существует", newEmail);
                throw new EmailExistsException("Данный email уже существует: " + newEmail);
            }
            existingUser.setEmail(newEmail);
        }
        log.info("Обновление пользователя: ", existingUser);
        userList.put(user.getId(), existingUser);
        return existingUser;
    }

    /**
     * Удаление пользователя
     */
    @Override
    public void deleteUser(Integer id) {
        if (userList.containsKey(id)) {
            log.info("Удаление пользователя с id: {}", id);
            userList.remove(id);
        } else {
            log.warn("Пользователь с данным id = {} не найден: ", id);
            throw new UserNotFoundException("Пользователь с данным id не найден: " + id);
        }
    }
}