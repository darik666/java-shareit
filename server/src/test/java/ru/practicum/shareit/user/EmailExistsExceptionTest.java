package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.exception.EmailExistsException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailExistsExceptionTest {

    @Test
    public void testEmailExistsException() {
        String errorMessage = "Email already exists";
        EmailExistsException exception = new EmailExistsException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}