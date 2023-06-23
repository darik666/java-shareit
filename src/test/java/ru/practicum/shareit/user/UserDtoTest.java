package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDtoTest {

    @Test
    public void testUserModel() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("TestUser");
        userDto.setEmail("testuser@test.com");

        assertEquals(1L, userDto.getId());
        assertEquals("TestUser", userDto.getName());
        assertEquals("testuser@test.com", userDto.getEmail());
    }
}
