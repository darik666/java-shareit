package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookingMapperTest {

    @Test
    public void toBookingDtoTest() {
        User user = new User(1L, "name", "name@email.com");
        ItemRequest itemRequest = new ItemRequest(
                1L, "desc", user, LocalDateTime.now(), new ArrayList<>());
        Item item = new Item(1L, "name", "descr", true, user, itemRequest);

        Booking booking = new Booking(1L, user, LocalDateTime.now(),
                LocalDateTime.now().plusHours(5), item, Status.WAITING);

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        Assertions.assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        Assertions.assertThat(bookingDto.getStart()).isEqualTo(booking.getStart());
        Assertions.assertThat(bookingDto.getEnd()).isEqualTo(booking.getEnd());
    }

    @Test
    public void toBookingTest() {
        BookingDto bookingDto = new BookingDto(
                1L, LocalDateTime.now(), LocalDateTime.now().plusHours(6), 1L);

        Booking booking = BookingMapper.toBooking(bookingDto);

        Assertions.assertThat(booking.getStart()).isEqualTo(booking.getStart());
        Assertions.assertThat(booking.getEnd()).isEqualTo(booking.getEnd());
    }

    @Test
    public void toBookingResponseDtoTest() {
        User user = new User(1L, "name", "name@email.com");
        ItemRequest itemRequest = new ItemRequest(
                1L, "desc", user, LocalDateTime.now(), new ArrayList<>());
        Item item = new Item(1L, "name", "descr", true, user, itemRequest);

        Booking booking = new Booking(1L, user, LocalDateTime.now(),
                LocalDateTime.now().plusHours(5), item, Status.WAITING);

        BookingResponseDto bookingResponseDto = BookingMapper.toBookingResponseDto(booking);

        Assertions.assertThat(bookingResponseDto.getId()).isEqualTo(booking.getId());
        Assertions.assertThat(bookingResponseDto.getStart()).isEqualTo(booking.getStart());
        Assertions.assertThat(bookingResponseDto.getEnd()).isEqualTo(booking.getEnd());
        Assertions.assertThat(bookingResponseDto.getItem()).isEqualTo(booking.getItem());
        Assertions.assertThat(bookingResponseDto.getBooker()).isEqualTo(booking.getBooker());
        Assertions.assertThat(bookingResponseDto.getStatus()).isEqualTo(booking.getStatus());
    }

    @Test
    public void toBookingShortDtoTest() {
        User user = new User(1L, "name", "name@email.com");
        ItemRequest itemRequest = new ItemRequest(
                1L, "desc", user, LocalDateTime.now(), new ArrayList<>());
        Item item = new Item(1L, "name", "descr", true, user, itemRequest);

        Booking booking = new Booking(1L, user, LocalDateTime.now(),
                LocalDateTime.now().plusHours(5), item, Status.WAITING);

        BookingShortDto bookingShortDto = BookingMapper.toBookingShortDto(booking);

        Assertions.assertThat(bookingShortDto.getId()).isEqualTo(booking.getId());
        Assertions.assertThat(bookingShortDto.getItem()).isEqualTo(booking.getItem());
        Assertions.assertThat(bookingShortDto.getBooker()).isEqualTo(booking.getBooker());
        Assertions.assertThat(bookingShortDto.getStatus()).isEqualTo(booking.getStatus());
    }
}
