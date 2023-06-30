package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {

    @Test
    public void testEnumValues() {
        assertEquals("WAITING", Status.WAITING.name());
        assertEquals("APPROVED", Status.APPROVED.name());
        assertEquals("REJECTED", Status.REJECTED.name());
        assertEquals("CANCELED", Status.CANCELED.name());
    }
}
