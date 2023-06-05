package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

/**
 * Интерфейс сервисного класса бронирований
 */
@Component
public interface BookingService {

    BookingResponseDto getBooking(Long ownerId, Long bookingId);

    List<BookingResponseDto> getAllBookings(Long ownerId, String state);

    BookingResponseDto postBooking(BookingDto bookingDto, Long ownerId);

    BookingResponseDto approveBooking(Long ownerId, Long bookingId, Boolean approved);

    List<BookingResponseDto> getAllOwnerBookings(Long ownerId, String state);
}