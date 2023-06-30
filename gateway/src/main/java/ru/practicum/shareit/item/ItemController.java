package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

/**
 * Контроллер вещей
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;
    
    /**
     * Получение вещей
     */
    @GetMapping
    public ResponseEntity<Object> getItems(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemClient.getItems(ownerId);
    }

    /**
     * Получение вещи по id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Long id,
                                          @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemClient.getItemById(id, ownerId);
    }

    /**
     * Добавление вещи
     */
    @PostMapping
    public ResponseEntity<Object> postItem(@Valid @RequestBody ItemDto itemDto,
                            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemClient.postItem(itemDto, ownerId);
    }

    /**
     * Обновление вещи
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long id,
                              @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemClient.updateItem(itemDto, ownerId, id);
    }

    /**
     * Удаление вещи
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        return itemClient.deleteItem(id);
    }

    /**
     * Поиск вещей
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam(required = true) String text) {
        return itemClient.searchItems(text);
    }

    /**
     * Комментарий к вещи
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@PathVariable Long itemId,
                                  @Valid @RequestBody Comment comment,
                                  @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        return itemClient.postComment(itemId, comment, ownerId);
    }

    /**
     * Поиск по комментариям
     */
    @GetMapping("/comments")
    public ResponseEntity<Object> searchComments(
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String text) {
        return itemClient.searchComments(itemId, authorId, text);
    }
}