package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {

    @Test
    public void testEnumValues() {
        Assertions.assertEquals("WAITING", Status.WAITING.name());
        Assertions.assertEquals("APPROVED", Status.APPROVED.name());
        Assertions.assertEquals("REJECTED", Status.REJECTED.name());
        Assertions.assertEquals("CANCELED", Status.CANCELED.name());
    }
}
