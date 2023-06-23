package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestMapperTest {

    @Test
    public void toItemRequestTest() {
        ItemRequestDto itemRequestDto =
                new ItemRequestDto(1L, "Desc", new User(), LocalDateTime.now(),
                        List.of(new ItemDto(1L, "Name", "Desc", true, 1L)));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        assertThat(itemRequest.getId()).isEqualTo(itemRequestDto.getId());
        assertThat(itemRequest.getDescription()).isEqualTo(itemRequestDto.getDescription());
        assertThat(itemRequest.getRequestor()).isEqualTo(itemRequestDto.getRequestor());
        assertThat(itemRequest.getCreated()).isEqualTo(itemRequestDto.getCreated());
    }

    @Test
    public void toItemRequestDtoTest() {
        ItemRequest itemRequest =
                new ItemRequest(1L, "Desc", new User(), LocalDateTime.now(), List.of(new Item()));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        assertThat(itemRequestDto.getId()).isEqualTo(itemRequest.getId());
        assertThat(itemRequestDto.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(itemRequestDto.getRequestor()).isEqualTo(itemRequest.getRequestor());
        assertThat(itemRequestDto.getCreated()).isEqualTo(itemRequest.getCreated());
    }
}