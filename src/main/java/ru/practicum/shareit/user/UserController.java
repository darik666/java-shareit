package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер пользователей
 */
@RestController
@RequestMapping(path = "/users")
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
    public UserDto getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    /**
     * Создание пользователя
     */
    @PostMapping
    public UserDto postUser(@Valid @RequestBody UserDto userDto) {
        return userService.postUser(userDto);
    }

    /**
     * Обновление пользователя
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody UserDto userDto,
                              @PathVariable Integer id) {
        userDto.setId(id);
        return userService.updateUser(userDto);
    }

    /**
     * Удаление пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}