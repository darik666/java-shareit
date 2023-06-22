package ru.practicum.shareit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {

    @Test
    public void testErrorResponse() {
        String error = "An error occurred";
        ErrorResponse errorResponse = new ErrorResponse(error);

        assertEquals(error, errorResponse.getError());
    }
}
