package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRequestService itemRequestService;

    @Test
    @Transactional
    void getItemRequestsTest() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(3);
        UserDto userDto = new UserDto(null, "TestUser", "testUser@test.com");
        UserDto userDto2 = new UserDto(null, "TestUser2", "testUser2@test.com");
        ItemDto itemDto = new ItemDto(null, "ItemName", "ItemDesc", true, null);
        BookingDto bookingDto = new BookingDto(
                null, start, end, 1L);
        userService.postUser(userDto);
        userService.postUser(userDto2);
        itemService.postItem(itemDto, 1L);
        bookingService.postBooking(bookingDto, 2L);
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L, "RequestDesc", UserMapper.toUser(userDto2), LocalDateTime.now(), null);
        itemRequestService.postItemRequest(itemRequestDto, 1L);

        List<ItemRequestDto> resultList = itemRequestService.getItemRequests(null, null, 2L);

        assertEquals(1, resultList.size());
        assertEquals(1L, resultList.get(0).getId());
        assertEquals(itemRequestDto.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemRequestDto.getRequestor(), resultList.get(0).getRequestor());
    }
}