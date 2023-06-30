package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер запроса вещей
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    /**
     * Создание запроса вещи
     */
    @PostMapping
    public ItemRequestDto postItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                          @RequestHeader(value = "X-Sharer-User-Id",
                                                  required = true) Long requestorId) {
        return itemRequestService.postItemRequest(itemRequestDto, requestorId);
    }

    /**
     * Получение запросов пользователя
     */
    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequestor(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long requestorId) {
        return itemRequestService.getItemRequestsByRequestor(requestorId);
    }

    /**
     * Получение запроса по id
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long requestId,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long requestorId) {
        return itemRequestService.getItemRequestById(requestId, requestorId);
    }

    /**
     * Получение запросов вещей
     */
    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(@RequestParam(defaultValue = "0") Long from,
                                                @RequestParam(defaultValue = "20") Long size,
                                                @RequestHeader(value = "X-Sharer-User-Id", required = true) Long requestorId) {
        return itemRequestService.getItemRequests(from, size, requestorId);
    }
}