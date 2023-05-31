package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс сервисного класса вещей
 */
@Component
public interface ItemService {
    List<ItemDto> getItems(Integer ownerId);

    ItemDto getItemById(Integer id);

    ItemDto postItem(ItemDto itemDto, Integer ownerId);

    ItemDto updateItem(ItemDto itemDto, Integer ownerId);

    void deleteItem(Integer id);

    List<ItemDto> searchItems(String text);
}