package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер вещей
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Получение вещей
     */
    @GetMapping
    public List<ItemDto> getItems(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Integer ownerId) {
        return itemService.getItems(ownerId);
    }

    /**
     * Получение вещи по id
     */
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    /**
     * Добавление вещи
     */
    @PostMapping
    public ItemDto postItem(@Valid @RequestBody ItemDto itemDto,
                            @RequestHeader(value = "X-Sharer-User-Id", required = true) Integer ownerId) {
        return itemService.postItem(itemDto, ownerId);
    }

    /**
     * Обновление вещи
     */
    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Integer id,
                              @RequestHeader(value = "X-Sharer-User-Id", required = true) Integer ownerId) {
        itemDto.setId(id);
        return itemService.updateItem(itemDto, ownerId);
    }

    /**
     * Удаление вещи
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
    }

    /**
     * Поиск вещей
     */
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}