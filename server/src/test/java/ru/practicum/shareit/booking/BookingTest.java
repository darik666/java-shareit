package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

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

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(bookingId, booking.getId());
        Assertions.assertEquals(booker, booking.getBooker());
        Assertions.assertEquals(start, booking.getStart());
        Assertions.assertEquals(end, booking.getEnd());
        Assertions.assertEquals(item, booking.getItem());
        Assertions.assertEquals(status, booking.getStatus());
    }
}
