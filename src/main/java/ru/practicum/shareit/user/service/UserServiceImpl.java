package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс пользователей
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserDao dao;

    @Autowired
    public UserServiceImpl(UserDao dao) {
        this.dao = dao;
    }

    /**
     * Получение пользователей
     */
    @Override
    public List<UserDto> getUsers() {
        return dao.getUsers().stream()
                .map(user -> UserMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

    /**
     * Получение пользователя по id
     */
    @Override
    public UserDto getUserById(Integer id) {
        return UserMapper.toUserDto(dao.getUserById(id));
    }

    /**
     * Создание пользователя
     */
    @Override
    public UserDto postUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(dao.postUser(user));
    }

    /**
     * Обновление пользователя
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = dao.updateUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    /**
     * Удаление пользователя
     */
    @Override
    public void deleteUser(Integer id) {
        dao.deleteUser(id);
    }
}