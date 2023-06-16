package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

/**
 * Интерфейс сервисного класса вещей
 */
public interface ItemService {
    List<ItemDtoWithBooking> getItems(Long ownerId);

    ItemDtoWithBooking getItemById(Long itemId, Long ownerId);

    ItemDto postItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long ownerId);

    void deleteItem(Long id);

    List<ItemDto> searchItems(String text);

    CommentDto postComment(Long itemId, Comment comment, Long ownerId);

    List<CommentDto> searchComments(Long itemId, Long authorId, String text);
}