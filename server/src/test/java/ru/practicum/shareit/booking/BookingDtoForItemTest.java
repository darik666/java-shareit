package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.user.model.User;

public class BookingDtoForItemTest {

    @Test
    public void testBookingCreation() {
        Long bookingId = 1L;
        User booker = new User(1L, "name", "name@email.com");

        BookingDtoForItem bookingDtoForItem = new BookingDtoForItem(bookingId, booker.getId());

        Assertions.assertNotNull(bookingDtoForItem);
        Assertions.assertEquals(bookingId, bookingDtoForItem.getId());
        Assertions.assertEquals(booker.getId(), bookingDtoForItem.getBookerId());
    }
}
