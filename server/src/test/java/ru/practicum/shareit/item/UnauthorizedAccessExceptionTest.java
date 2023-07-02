package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.exception.UnauthorizedAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnauthorizedAccessExceptionTest {

    @Test
    public void testUnauthorizedAccessExceptionMessage() {
        String errorMessage = "Unauthorized access";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
