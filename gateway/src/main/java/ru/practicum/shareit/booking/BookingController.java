package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	/**
	 * Получение всех бронирований
	 */
	@GetMapping
	public ResponseEntity<Object> getAllBookings(
			@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
			@RequestParam(defaultValue = "ALL") String state,
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Positive @RequestParam(defaultValue = "10") Integer size) {
		BookingState state1 = BookingState.from(state)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
		log.info("Get booking with state {}, userId={}, from={}, size={}", state, ownerId, from, size);
		return bookingClient.getBookings(ownerId, state1, from, size);
	}

	/**
	 * Сохранение бронирования
	 */
	@PostMapping
	public ResponseEntity<Object> postBooking(@Valid @RequestBody BookingDto bookingDto,
										  @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
		log.info("Creating booking {}, userId={}", bookingDto, ownerId);
		return bookingClient.bookItem(ownerId, bookingDto);
	}

	/**
	 * Получение бронирования по id
	 */
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
										 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, ownerId);
		return bookingClient.getBooking(ownerId, bookingId);
	}

	/**
	 * Подтверждение бронирования
	 */
	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
											 @PathVariable Long bookingId,
											 @RequestParam Boolean approved) {
		return bookingClient.approveBooking(ownerId, bookingId, approved);
	}

	/**
	 * Получение всех бронирований владельца
	 */
	@GetMapping("/owner")
	public ResponseEntity<Object> getAllOwnerBookings(
			@RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
			@RequestParam(defaultValue = "ALL") String state,
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Positive @RequestParam(defaultValue = "10") Integer size) {
		State.from(state).orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));
		return bookingClient.getAllOwnerBookings(ownerId, BookingState.from(state).get(), from, size);
	}
}
