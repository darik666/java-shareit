package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingResponseDtoTest {

    @Test
    public void testBookingResponseDtoCreation() {
        Long bookingId = 1L;
        User booker = new User();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        Item item = new Item();
        Status status = Status.WAITING;

        BookingResponseDto bookingResponseDto = new BookingResponseDto(
                bookingId, start, end, item, booker, status);

        Assertions.assertNotNull(bookingResponseDto);
        Assertions.assertEquals(bookingId, bookingResponseDto.getId());
        Assertions.assertEquals(booker, bookingResponseDto.getBooker());
        Assertions.assertEquals(start, bookingResponseDto.getStart());
        Assertions.assertEquals(end, bookingResponseDto.getEnd());
        Assertions.assertEquals(item, bookingResponseDto.getItem());
        Assertions.assertEquals(status, bookingResponseDto.getStatus());
    }
}