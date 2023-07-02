package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeniedCommentingExceptionTest {

    @Test
    public void testDeniedCommentingExceptionMessage() {
        String errorMessage = "Item not found";
        DeniedCommentingException exception = new DeniedCommentingException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
