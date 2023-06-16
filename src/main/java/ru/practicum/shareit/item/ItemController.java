package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер вещей
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    /**
     * Получение вещей
     */
    @GetMapping
    public List<ItemDtoWithBooking> getItems(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemService.getItems(ownerId);
    }

    /**
     * Получение вещи по id
     */
    @GetMapping("/{id}")
    public ItemDtoWithBooking getItemById(@PathVariable Long id,
                                          @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemService.getItemById(id, ownerId);
    }

    /**
     * Добавление вещи
     */
    @PostMapping
    public ItemDto postItem(@Valid @RequestBody ItemDto itemDto,
                            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemService.postItem(itemDto, ownerId);
    }

    /**
     * Обновление вещи
     */
    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long id,
                              @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        itemDto.setId(id);
        return itemService.updateItem(itemDto, ownerId);
    }

    /**
     * Удаление вещи
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }

    /**
     * Поиск вещей
     */
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(required = true) String text) {
        return itemService.searchItems(text);
    }

    /**
     * Комментарий к вещи
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@PathVariable Long itemId,
                                  @Valid @RequestBody Comment comment,
                                  @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemService.postComment(itemId, comment, ownerId);
    }

    /**
     * Поиск по комментариям
     */
    @GetMapping("/comments")
    public List<CommentDto> searchComments(
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String text) {
        return itemService.searchComments(itemId, authorId, text);
    }
}