package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс хранилища вещей
 */
@Component
public interface ItemDao {
    List<Item> getItems(Integer ownerId);

    Item getItemById(Integer id);

    Item postItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Integer id);

    List<Item> searchItems(String text);
}