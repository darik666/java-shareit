package ru.practicum.shareit.item.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UnauthorizedAccessException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Хранилище вещей в памяти
 */
@Primary
@Repository
public class ItemMemoryDao implements ItemDao {
    private HashMap<Integer, Item> itemList = new HashMap<>();
    private Integer id = 0;

    /**
     * Получение вещей
     */
    @Override
    public List<Item> getItems(Integer ownerId) {
        return itemList.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    /**
     * Получение вещи по id
     */
    @Override
    public Item getItemById(Integer id) {
        Optional<Item> item = Optional.ofNullable(itemList.get(id));
        return item.orElseThrow(() -> new ItemNotFoundException(
                String.format("Вещь с id %d не найдена", id)));
    }

    /**
     * Добавление вещи
     */
    @Override
    public Item postItem(Item item) {
        item.setId(++id);
        itemList.put(id, item);
        return item;
    }

    /**
     * Обновление вещи
     */
    @Override
    public Item updateItem(Item item) {
        Item existingItem = itemList.get(item.getId());
        if (existingItem == null) {
            throw new ItemNotFoundException("Вещь с данным id не найдена: " + item.getId());
        }
        if (!existingItem.getOwner().equals(item.getOwner())) {
            throw new UnauthorizedAccessException("Только владельцы вещи могут обновлять ее");
        }
        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            existingItem.setAvailable(item.getAvailable());
        }
        return existingItem;
    }

    /**
     * Удаление вещи
     */
    @Override
    public void deleteItem(Integer id) {
        if (itemList.containsKey(id)) {
            itemList.remove(id);
        } else {
            throw new ItemNotFoundException(String.format("Вещь с id %d не найдена", id));
        }
    }

    /**
     * Поиск вещей
     */
    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        String searchText = text.toLowerCase();
        return itemList.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(searchText)
                        || item.getDescription().toLowerCase().contains(searchText))
                .filter(item -> item.getAvailable())
                .collect(Collectors.toList());
    }
}