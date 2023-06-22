package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    @Test
    public void toUserDtoTest() {
        User user = new User(1L, "Adam", "adamant@mail.com");
        UserDto userDto = UserMapper.toUserDto(user);
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void toUserTest() {
        UserDto userDto = new UserDto(1L, "Adam", "adamant@mail.com");
        User user = UserMapper.toUser(userDto);
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getName()).isEqualTo(userDto.getName());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }
}