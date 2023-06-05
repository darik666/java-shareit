package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервисный класс вещей
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    /**
     * Получение вещей
     */
    @Transactional
    @Override
    public List<ItemDtoWithBooking> getItems(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        List<ItemDtoWithBooking> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(putBookings(item, ownerId));
        }
        log.debug("Получение списка вещей: " + itemDtos);
        return itemDtos;
    }

    /**
     * Добавление бронирований к вещам
     */
    public ItemDtoWithBooking putBookings(Item item, Long ownerId) {
        ItemDtoWithBooking itemDto = ItemMapper.toItemDtoWithBooking(item);

        List<Booking> lastBook = bookingRepository
                .findLastBookingForItem(item.getId(), ownerId);
        List<Booking> nextBook = bookingRepository
                .findNextBookingForItem(item.getId(), ownerId);

        log.debug("Добавление бронирований к вещи, lastBook: " + lastBook +
                " и nextBook" + nextBook);

        if (!lastBook.isEmpty()) {
            BookingDtoForItem lastBooking = new BookingDtoForItem(
                    lastBook.get(0).getId(), lastBook.get(0).getBooker().getId());
            itemDto.setLastBooking(lastBooking);
        }

        if (!nextBook.isEmpty()) {
            BookingDtoForItem nextBooking = new BookingDtoForItem(
                    nextBook.get(0).getId(), nextBook.get(0).getBooker().getId());
            itemDto.setNextBooking(nextBooking);
        }
        return itemDto;
    }

    /**
     * Получение вещи по id
     */
    @Transactional
    @Override
    public ItemDtoWithBooking getItemById(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId).get();
        ItemDtoWithBooking result = putBookings(item, ownerId);
        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
        result.setComments(comments);
        log.debug("Получение вещи по id: " + result);
        return result;
    }

    /**
     * Добавление вещи
     */
    @Transactional
    @Override
    public ItemDto postItem(ItemDto itemDto, Long ownerId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(ownerId).get());
        log.debug("Сохранение вещи: " + item);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    /**
     * Обновление вещи
     */
    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, Long ownerId) {
        Item existingItem = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new ItemNotFoundException(
                        String.format("Вещь с id=%d не найден", itemDto.getId())));
        if (!Objects.equals(existingItem.getOwner().getId(), ownerId)) {
            log.warn("Редактировать вещь может только её владелец.");
            throw new RuntimeException("Редактировать вещь может только её владелец.");
        }
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(existingItem);
        log.debug("Обновление вещи " + updatedItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    /**
     * Удаление вещи
     */
    @Transactional
    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
        log.debug("Удаление вещи по id: " + id);
    }

    /**
     * Поиск вещей
     */
    @Transactional
    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        log.debug("Поиск вещи по тексту: " + text);
        List<Item> items = itemRepository
                .searchByText(text.toLowerCase());
        return items.stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    /**
     * Комментарий к вещи
     */
    @Transactional
    @Override
    public CommentDto postComment(Long itemId, Comment comment, Long ownerId) {
        List<Booking> bookings =
                bookingRepository.findByItemIdAndOwnerId(itemId, ownerId);

        if (!bookings.isEmpty()) {
            User user = userRepository.findById(ownerId)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNotFoundException("Вещь не найдена"));

            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreateTime(LocalDateTime.now());
            log.debug("Сохранение отзыва о вещи: " + comment);
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        } else {
            log.warn("Пользователь не бронировал вещь или бронирование не завершено");
            throw new DeniedCommentingException("Пользователь не бронировал вещь или бронирование не завершено");
        }
    }

    /**
     * Поиск по комментариям
     */
    @Override
    public List<CommentDto> searchComments(Long itemId, Long authorId, String text) {
        List<Comment> comments = new ArrayList<>();

        if (itemId != null) {
            comments = commentRepository.findByItemId(itemId);
        }

        if (authorId != null) {
            List<Comment> authorComments = commentRepository.findByAuthorId(authorId);
            if (comments.isEmpty()) {
                comments = authorComments;
            } else {
                comments.retainAll(authorComments);
            }
        }

        if (text != null) {
            List<Comment> textComments = commentRepository.findByTextContainingIgnoreCase(text);
            if (comments.isEmpty()) {
                comments = textComments;
            } else {
                comments.retainAll(textComments);
            }
        }

        return comments.stream()
                .map(comment -> CommentMapper.toCommentDto(comment))
                .collect(Collectors.toList());
    }
}