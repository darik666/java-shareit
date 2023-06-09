package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
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
    public List<BookingResponseDto> getAllBookings(Long ownerId, String state, Integer page, Integer size) {
        findUser(ownerId);
        log.debug("Получение бронирований по id владельца и состоянию: " + state);
        int adjustedPage = (page + size - 1) / size;
        Page<Booking> bookingPage;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        switch (state) {
            case "ALL":
                bookingPage = bookingRepository.findAllBookings(ownerId, pageable);
                break;
            case "CURRENT":
                bookingPage = bookingRepository.findCurrentBookings(ownerId, pageable);
                break;
            case "PAST":
                bookingPage = bookingRepository.findPastBookings(ownerId, pageable);
                break;
            case "FUTURE":
                bookingPage = bookingRepository.findFutureBookings(ownerId, pageable);
                break;
            case "WAITING":
                bookingPage = bookingRepository.findBookingsByWaiting(ownerId, pageable);
                break;
            case "REJECTED":
                bookingPage = bookingRepository.findBookingsByRejected(ownerId, pageable);
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: " + state);
        }
        List<BookingResponseDto> bookingDtos = bookingPage.map(BookingMapper::toBookingResponseDto).getContent();
        return bookingDtos;
    }

    /**
     * Получение всех бронирований владельца
     */
    @Override
    public List<BookingResponseDto> getAllOwnerBookings(Long ownerId, String state, Integer page, Integer size) {
        findUser(ownerId);
        log.debug("Получение бронирований по id владельца и состоянию: " + state);
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        Page<Booking> bookingPage;

        switch (state) {
            case "ALL":
                bookingPage = bookingRepository.findOwnerAllBookings(ownerId, pageable);
                break;
            case "CURRENT":
                bookingPage = bookingRepository.findOwnerCurrentBookings(ownerId, pageable);
                break;
            case "PAST":
                bookingPage = bookingRepository.findOwnerPastBookings(ownerId, pageable);
                break;
            case "FUTURE":
                bookingPage = bookingRepository.findOwnerFutureBookings(ownerId, pageable);
                break;
            case "WAITING":
                bookingPage = bookingRepository.findOwnerBookingsByWaiting(ownerId, pageable);
                break;
            case "REJECTED":
                bookingPage = bookingRepository.findOwnerBookingsByRejected(ownerId, pageable);
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: " + state);
        }
        List<BookingResponseDto> bookingDtos = bookingPage.map(BookingMapper::toBookingResponseDto).getContent();
        return bookingDtos;
    }

    /**
     * Сохранение бронирования
     */
    @Transactional
    @Override
    public BookingResponseDto postBooking(BookingDto bookingDto, Long ownerId) {
        User booker = findUser(ownerId);
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
        booking = bookingRepository.save(booking);
        log.debug("Сохранение бронирования " + booking);
        return BookingMapper.toBookingResponseDto(booking);
    }

    /**
     * Подтверждение бронирования
     */
    @Transactional
    @Override
    public BookingResponseDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        findUser(ownerId);
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

    public User findUser(Long ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }
}