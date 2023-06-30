package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * Контроллер пользователей
 */
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    /**
     * Получение пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    /**
     * Получение пользователя по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        return userClient.getUserById(id);
    }

    /**
     * Создание пользователя
     */
    @PostMapping
    @Validated(UserDto.OnCreate.class)
    public ResponseEntity<Object> postUser(@RequestBody @Valid UserDto userDto) {
        return userClient.postUser(userDto);
    }

    /**
     * Обновление пользователя
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long id) {
        return userClient.updateUser(userDto, id);
    }

    /**
     * Удаление пользователя
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userClient.deleteUser(id);
    }
}