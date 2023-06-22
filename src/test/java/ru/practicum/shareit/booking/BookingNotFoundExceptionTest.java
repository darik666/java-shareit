package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingNotFoundExceptionTest {

    @Test
    public void testBookingNotFoundExceptionMessage() {
        String errorMessage = "Booking not found";
        BookingNotFoundException exception = new BookingNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
