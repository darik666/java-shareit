package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

/**
 * Интерфейс сервисного класса бронирований
 */
@Component
public interface BookingService {

    Booking getBooking(Long ownerId, Long bookingId);

    List<Booking> getAllBookings(Long ownerId, String state);

    Booking postBooking(BookingDto bookingDto, Long ownerId);

    Booking approveBooking(Long ownerId, Long bookingId, Boolean approved);

    List<Booking> getAllOwnerBookings(Long ownerId, String state);
}