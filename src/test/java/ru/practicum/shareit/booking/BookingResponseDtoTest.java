package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        assertNotNull(bookingResponseDto);
        assertEquals(bookingId, bookingResponseDto.getId());
        assertEquals(booker, bookingResponseDto.getBooker());
        assertEquals(start, bookingResponseDto.getStart());
        assertEquals(end, bookingResponseDto.getEnd());
        assertEquals(item, bookingResponseDto.getItem());
        assertEquals(status, bookingResponseDto.getStatus());
    }
}