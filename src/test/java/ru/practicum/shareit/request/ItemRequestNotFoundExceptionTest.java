package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestNotFoundExceptionTest {

    @Test
    public void testItemRequestNotFoundException() {
        String errorMessage = "Item request not found";
        ItemRequestNotFoundException exception = new ItemRequestNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
