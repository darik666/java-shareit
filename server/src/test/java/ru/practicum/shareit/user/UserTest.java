package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Test
    public void testUserModel() {
        User user = new User();
        user.setId(1L);
        user.setName("TestUser");
        user.setEmail("testuser@test.com");

        assertEquals(1L, user.getId());
        assertEquals("TestUser", user.getName());
        assertEquals("testuser@test.com", user.getEmail());
    }
}
