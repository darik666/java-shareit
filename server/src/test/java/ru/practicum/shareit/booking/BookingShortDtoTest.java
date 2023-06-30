package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingShortDtoTest {

    @Test
    public void testBookingShortDtoCreation() {
        Long bookingId = 1L;
        User booker = new User();
        Item item = new Item();
        Status status = Status.WAITING;

        BookingShortDto bookingShortDto = new BookingShortDto(bookingId, item, booker, status);

        Assertions.assertNotNull(bookingShortDto);
        Assertions.assertEquals(bookingId, bookingShortDto.getId());
        Assertions.assertEquals(booker, bookingShortDto.getBooker());
        Assertions.assertEquals(item, bookingShortDto.getItem());
        Assertions.assertEquals(status, bookingShortDto.getStatus());
    }
}