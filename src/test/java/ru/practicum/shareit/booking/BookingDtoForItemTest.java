package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingDtoForItemTest {

    @Test
    public void testBookingCreation() {
        Long bookingId = 1L;
        User booker = new User(1L, "name", "name@email.com");

        BookingDtoForItem bookingDtoForItem = new BookingDtoForItem(bookingId, booker.getId());

        assertNotNull(bookingDtoForItem);
        assertEquals(bookingId, bookingDtoForItem.getId());
        assertEquals(booker.getId(), bookingDtoForItem.getBookerId());
    }
}
