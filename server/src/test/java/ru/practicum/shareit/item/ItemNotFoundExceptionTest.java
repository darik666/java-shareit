package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.exception.ItemNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemNotFoundExceptionTest {

    @Test
    public void testItemNotFoundExceptionMessage() {
        String errorMessage = "Item not found";
        ItemNotFoundException exception = new ItemNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
