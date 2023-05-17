package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс вещей
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    @Autowired
    public ItemServiceImpl(ItemDao itemDao, UserDao userDao) {
        this.itemDao = itemDao;
        this.userDao = userDao;
    }

    /**
     * Получение вещей
     */
    @Override
    public List<ItemDto> getItems(Integer ownerId) {
        return itemDao.getItems(ownerId).stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    /**
     * Получение вещи по id
     */
    @Override
    public ItemDto getItemById(Integer id) {
        return ItemMapper.toItemDto(itemDao.getItemById(id));
    }

    /**
     * Добавление вещи
     */
    @Override
    public ItemDto postItem(ItemDto itemDto, Integer ownerId) {
        itemDto.setOwner(userDao.getUserById(ownerId));
        Item item = itemDao.postItem(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    /**
     * Обновление вещи
     */
    @Override
    public ItemDto updateItem(ItemDto itemDto, Integer ownerId) {
        itemDto.setOwner(userDao.getUserById(ownerId));
        Item item = itemDao.updateItem(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    /**
     * Удаление вещи
     */
    @Override
    public void deleteItem(Integer id) {
        itemDao.deleteItem(id);
    }

    /**
     * Поиск вещей
     */
    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = itemDao.searchItems(text);
        return items.stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }
}