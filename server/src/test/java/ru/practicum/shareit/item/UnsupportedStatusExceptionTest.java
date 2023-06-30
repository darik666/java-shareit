package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.exception.UnsupportedStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnsupportedStatusExceptionTest {

    @Test
    public void testUnsupportedStatusExceptionMessage() {
        String errorMessage = "Unsupported status";
        UnsupportedStatusException exception = new UnsupportedStatusException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
