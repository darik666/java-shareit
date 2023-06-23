package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingTest {

    @Test
    public void testBookingCreation() {
        Long bookingId = 1L;
        User booker = new User();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        Item item = new Item();
        Status status = Status.WAITING;

        Booking booking = new Booking(bookingId, booker, start, end, item, status);

        assertNotNull(booking);
        assertEquals(bookingId, booking.getId());
        assertEquals(booker, booking.getBooker());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(status, booking.getStatus());
    }
}
