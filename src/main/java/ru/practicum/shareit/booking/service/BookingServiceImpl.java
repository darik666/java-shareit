package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UnauthorizedAccessException;
import ru.practicum.shareit.item.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.Status.*;

/**
 * Сервисный класс бронирований
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * Получение бронирования по id
     */
    @Override
    public BookingResponseDto getBooking(Long ownerId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));
        User booker = booking.getBooker();
        User itemOwner = booking.getItem().getOwner();
        if (!booker.getId().equals(ownerId)
                && !itemOwner.getId().equals(ownerId)) {
            log.warn("Пользователь не является создателем вещи или бронирования");
            throw new UnauthorizedAccessException("Пользователь не является создателем вещи или бронирования");
        }
        log.debug("Получение бронирования: " + booking);
        return BookingMapper.toBookingResponseDto(booking);
    }

    /**
     * Получение всех бронирований
     */
    @Override
    public List<BookingResponseDto> getAllBookings(Long ownerId, String state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        log.debug("Получение бронирований по id владельца и состоянию: " + state);
        switch (state) {
            case "ALL": return bookingRepository.findAllBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "CURRENT": return bookingRepository.findCurrentBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "PAST": return bookingRepository.findPastBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "FUTURE": return bookingRepository.findFutureBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "WAITING": return bookingRepository.findBookingsByWaiting(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "REJECTED": return bookingRepository.findBookingsByRejected(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            default: throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }

    /**
     * Получение всех бронирований владельца
     */
    @Override
    public List<BookingResponseDto> getAllOwnerBookings(Long ownerId, String state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        log.debug("Получение бронирований по id владельца и состоянию: " + state);
        switch (state) {
            case "ALL": return bookingRepository.findOwnerAllBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "CURRENT": return bookingRepository.findOwnerCurrentBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "PAST": return bookingRepository.findOwnerPastBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "FUTURE": return bookingRepository.findOwnerFutureBookings(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "WAITING": return bookingRepository.findOwnerBookingsByWaiting(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            case "REJECTED": return bookingRepository.findOwnerBookingsByRejected(ownerId).stream()
                    .map(booking -> BookingMapper.toBookingResponseDto(booking))
                    .collect(Collectors.toList());
            default: throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }

    /**
     * Сохранение бронирования
     */
    @Transactional
    @Override
    public BookingResponseDto postBooking(BookingDto bookingDto, Long ownerId) {
        User booker = userRepository.findById(ownerId).orElseThrow(
                () -> new UserNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(
                () -> new ItemNotFoundException("Вещь не найдена"));
        if (!item.getAvailable()) {
            log.warn("Вещь недоступна для бронирования");
            throw new IllegalArgumentException("Вещь недоступна для бронирования");
        }
        if (item.getOwner().getId().equals(ownerId)) {
            log.warn("Нельзя бронировать вещь у себя");
            throw new UnauthorizedAccessException("Нельзя бронировать вещь у себя");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) ||
                bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            log.warn("Некорректные даты бронирования");
            throw new IllegalArgumentException("Некорректные даты бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(WAITING);
        log.debug("Сохранение бронирования " + booking);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    /**
     * Подтверждение бронирования
     */
    @Transactional
    @Override
    public BookingResponseDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));
        Status status = booking.getStatus();
        if (status.toString().equals("APPROVED")) {
            log.warn("Бронирование уже подтверждено");
            throw new IllegalArgumentException("Бронирование уже подтверждено");
        }
        User user = booking.getItem().getOwner();
        if (!user.getId().equals(ownerId)) {
            log.warn("Пользователь не является создателем вещи ");
            throw new UnauthorizedAccessException("Пользователь не является создателем вещи ");
        }
        if (approved) {
            log.debug("Подтверждение бронирования id=" + bookingId + ", статус APPROVED");
            booking.setStatus(APPROVED);
        } else {
            log.debug("Подтверждение бронирования id=" + bookingId + ", статус REJECTED");
            booking.setStatus(REJECTED);
        }
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }
}