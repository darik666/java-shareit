package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс пользователей
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    /**
     * Получение пользователей
     */
    @Transactional
    @Override
    public List<UserDto> getUsers() {
        log.debug("Получение списка всех пользователей");
        return repository.findAll().stream()
                .map(user -> UserMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

    /**
     * Получение пользователя по id
     */
    @Transactional
    @Override
    public UserDto getUserById(Long id) {
        log.debug("Получение пользователя с id = " + id);
        return UserMapper.toUserDto(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id=%d не найден", id))));
    }

    /**
     * Создание пользователя
     */
    @Transactional
    @Override
    public UserDto postUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.debug("Создание пользователя: " + userDto);
        return UserMapper.toUserDto(repository.save(user));
    }

    /**
     * Обновление пользователя
     */
    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto) {
        User existingUser = repository.findById(Long.valueOf(userDto.getId()))
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id=%d не найден", userDto.getId())));
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        User user = repository.save(existingUser);
        log.debug("Обновление пользователя: " + user);
        return UserMapper.toUserDto(user);
    }

    /**
     * Удаление пользователя
     */
    @Transactional
    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
        log.info("Удаление пользователя с id = " + id);
    }
}