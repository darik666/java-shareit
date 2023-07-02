package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemMapperTest {

    @Test
    public void toItemDtoTest() {
        User user = new User(1L, "name", "name@email.com");
        ItemRequest itemRequest = new ItemRequest(
                1L, "desc", user, LocalDateTime.now(), new ArrayList<>());
        Item item = new Item(1L, "name", "descr", true, user, itemRequest);

        ItemDto itemDto = ItemMapper.toItemDto(item);

        assertThat(itemDto.getId()).isEqualTo(item.getId());
        assertThat(itemDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDto.getName()).isEqualTo(item.getName());
        assertThat(itemDto.getAvailable()).isEqualTo(item.getAvailable());
        assertThat(itemDto.getRequestId()).isEqualTo(item.getOwner().getId());
    }

    @Test
    public void toItemTest() {
        ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);

        Item item = ItemMapper.toItem(itemDto);

        assertThat(item.getId()).isEqualTo(itemDto.getId());
        assertThat(item.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getAvailable()).isEqualTo(itemDto.getAvailable());
    }

    @Test
    public void toItemDtoWithBooking() {
        User user = new User(1L, "name", "name@email.com");
        ItemRequest itemRequest = new ItemRequest(
                1L, "desc", user, LocalDateTime.now(), new ArrayList<>());
        Item item = new Item(1L, "name", "descr", true, user, itemRequest);

        ItemDtoWithBooking itemDtoWithBooking = ItemMapper.toItemDtoWithBooking(item);

        assertThat(itemDtoWithBooking.getId()).isEqualTo(item.getId());
        assertThat(itemDtoWithBooking.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDtoWithBooking.getName()).isEqualTo(item.getName());
        assertThat(itemDtoWithBooking.getAvailable()).isEqualTo(item.getAvailable());
    }
}