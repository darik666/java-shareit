package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер бронирований
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Получение всех бронирований
     */
    @GetMapping
    public List<BookingResponseDto> getAllBookings(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
                                                   @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getAllBookings(ownerId, state);
    }

    /**
     * Получение бронирования по id
     */
    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
                              @PathVariable Long bookingId) {
        return bookingService.getBooking(ownerId, bookingId);
    }

    /**
     * Сохранение бронирования
     */
    @PostMapping
    public BookingResponseDto postBooking(@Valid @RequestBody BookingDto bookingDto,
                               @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return bookingService.postBooking(bookingDto, ownerId);
    }

    /**
     * Подтверждение бронирования
     */
    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved) {
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    /**
     * Получение всех бронирований владельца
     */
    @GetMapping("/owner")
    public List<BookingResponseDto> getAllOwnerBookings(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllOwnerBookings(ownerId, state);
    }
}