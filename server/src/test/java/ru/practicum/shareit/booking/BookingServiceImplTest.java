package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UnauthorizedAccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    User user1;
    User user2;
    Item item1;
    Item item2;
    Item item3;
    Comment comment1;
    Comment comment2;
    Booking booking1;
    Booking booking2;
    ItemRequest itemRequest1;

    @BeforeEach
    public void setUp() {
        user1 = new User(1L, "TestUser", "testuser@email.com");
        user2 = new User(2L, "TestUser2", "testuser2@email.com");
        item1 = new Item(1L, "Item1", "Item1-Desc", true, user1, null);
        item2 = new Item(2L, "Item2", "Item2-Desc", true, user1, null);
        item3 = new Item(3L, "Item3", "Item3-Desc", true, user2, null);
        booking1 = new Booking(1L, user1, LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1), item1, Status.WAITING);
        booking2 = new Booking(2L, user1, LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusHours(10), item1, Status.WAITING);
        comment1 = new Comment(1L, item1, user1, LocalDateTime.now(), "Comentar");
        comment2 = new Comment(2L, item1, user1, LocalDateTime.now(), "Comentar2");
        itemRequest1 = new ItemRequest(1L, "RequestDesc", user1, LocalDateTime.now(), new ArrayList<>());
    }


    @Test
    public void getBookingShouldReturnBooking() {
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(booking1));

        BookingResponseDto resultBooking = bookingService.getBooking(user1.getId(), booking1.getId());

        Assertions.assertEquals(booking1.getId(), resultBooking.getId());
        Assertions.assertEquals(booking1.getBooker(), resultBooking.getBooker());
        Assertions.assertEquals(booking1.getItem(), resultBooking.getItem());
    }

    @Test
    public void getBookingShouldThrowBookingNotFoundException() {
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBooking(user1.getId(), booking1.getId()));
    }

    @Test
    public void getBookingShouldThrowUnauthorizedAccessException() {
        booking1.setBooker(user2);
        booking1.getItem().setOwner(user2);
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(booking1));

        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> bookingService.getBooking(user1.getId(), booking1.getId()));
    }

    @Test
    public void getAllBookingsByAllStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findAllBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllBookings(user1.getId(), "ALL", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllBookingsByCurrentStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findCurrentBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllBookings(user1.getId(), "CURRENT", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllBookingsByPastStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findPastBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllBookings(user1.getId(), "PAST", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllBookingsByFutureStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findFutureBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllBookings(user1.getId(), "FUTURE", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllBookingsByWaitingStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findBookingsByWaiting(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllBookings(user1.getId(), "WAITING", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllBookingsByRejectedStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findBookingsByRejected(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllBookings(user1.getId(), "REJECTED", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllOwnerBookingsByAllStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findOwnerAllBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllOwnerBookings(user1.getId(), "ALL", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllOwnerBookingsByCurrentStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findOwnerCurrentBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllOwnerBookings(user1.getId(), "CURRENT", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllOwnerBookingsByPastStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findOwnerPastBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllOwnerBookings(user1.getId(), "PAST", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllOwnerBookingsByFutureStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findOwnerFutureBookings(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllOwnerBookings(user1.getId(), "FUTURE", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllOwnerBookingsByWaitingStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findOwnerBookingsByWaiting(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllOwnerBookings(user1.getId(), "WAITING", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void getAllOwnerBookingsByRejectedStateTest() {
        int page = 0;
        int size = 10;
        int adjustedPage = (page + size - 1) / size;
        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("id").ascending());
        List<Booking> mockedBookings = List.of(booking1, booking2);
        Page<Booking> mockedBookingsPage = new PageImpl<>(mockedBookings, pageable, mockedBookings.size());

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findOwnerBookingsByRejected(user1.getId(), pageable)).thenReturn(mockedBookingsPage);

        List<BookingResponseDto> resultBookList =
                bookingService.getAllOwnerBookings(user1.getId(), "REJECTED", 0, 10);

        Assertions.assertEquals(2, resultBookList.size());
        Assertions.assertEquals(booking1.getId(), resultBookList.get(0).getId());
        Assertions.assertEquals(booking1.getBooker(), resultBookList.get(0).getBooker());
        Assertions.assertEquals(booking2.getId(), resultBookList.get(1).getId());
        Assertions.assertEquals(booking2.getBooker(), resultBookList.get(1).getBooker());
    }

    @Test
    public void postBookingTestShouldCreate() {
        booking1.getItem().setOwner(user2);
        Mockito.when(bookingRepository.save(ArgumentMatchers.any())).thenReturn(booking1);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(item1));

        BookingResponseDto resultBooking =
                bookingService.postBooking(BookingMapper.toBookingDto(booking1), user1.getId());

        Assertions.assertEquals(booking1.getId(), resultBooking.getId());
        Assertions.assertEquals(booking1.getItem(), resultBooking.getItem());
        Assertions.assertEquals(booking1.getBooker(), resultBooking.getBooker());
    }

    @Test
    public void postBookingShouldThrowUserNotFoundExceptionTest() {
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.postBooking(BookingMapper.toBookingDto(booking1), user1.getId()));
    }

    @Test
    public void postBookingShouldThrowItemNotFoundExceptionTest() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> bookingService.postBooking(BookingMapper.toBookingDto(booking1), user1.getId()));
    }

    @Test
    public void postBookingWithUnavailableItemTest() {
        booking1.getItem().setAvailable(false);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(item1));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookingService.postBooking(BookingMapper.toBookingDto(booking1), user1.getId()));
    }

    @Test
    public void postBookingShouldThrowUnauthorizedAccessExceptionTest() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(item1));

        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> bookingService.postBooking(BookingMapper.toBookingDto(booking1), user1.getId()));
    }

    @Test
    public void postBookingWithIncorrectStartDateTest() {
        booking1.setStart(LocalDateTime.now().plusDays(20));
        booking1.getItem().setOwner(user2);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(item1));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookingService.postBooking(BookingMapper.toBookingDto(booking1), user1.getId()));
    }

    @Test
    public void approveBookingTestShouldApprove() {
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(booking1));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any())).thenReturn(booking1);

        BookingResponseDto resultBooking =
                bookingService.approveBooking(user1.getId(), booking1.getId(), true);

        Assertions.assertEquals(Status.APPROVED, resultBooking.getStatus());
        Assertions.assertEquals(booking1.getId(), resultBooking.getId());
        Assertions.assertEquals(booking1.getStart(), resultBooking.getStart());
    }

    @Test
    public void approveBookingTestShouldReject() {
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(booking1));
        Mockito.when(bookingRepository.save(ArgumentMatchers.any())).thenReturn(booking1);

        BookingResponseDto resultBooking =
                bookingService.approveBooking(user1.getId(), booking1.getId(), false);

        Assertions.assertEquals(Status.REJECTED, resultBooking.getStatus());
        Assertions.assertEquals(booking1.getId(), resultBooking.getId());
        Assertions.assertEquals(booking1.getStart(), resultBooking.getStart());
    }

    @Test
    public void approveBookingTestAlreadyBooked() {
        booking1.setStatus(Status.APPROVED);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(booking1));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookingService.approveBooking(user1.getId(), booking1.getId(), true));
    }

    @Test
    public void approveBookingNotOwnerTest() {
        booking1.getItem().setOwner(user2);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(bookingRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(booking1));

        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> bookingService.approveBooking(user1.getId(), booking1.getId(), true));
    }
}
