package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.*;
import java.util.List;

/**
 * Контроллер пользователей
 */
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получение пользователей
     */
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    /**
     * Получение пользователя по id
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Создание пользователя
     */
    @PostMapping
    @Validated(UserDto.OnCreate.class)
    public UserDto postUser(@RequestBody @Valid UserDto userDto) {
        return userService.postUser(userDto);
    }

    /**
     * Обновление пользователя
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long id) {
        userDto.setId(id);
        return userService.updateUser(userDto);
    }

    /**
     * Удаление пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}