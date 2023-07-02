package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

/**
 * Контроллер запроса вещей
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    /**
     * Создание запроса вещи
     */
    @PostMapping
    public ResponseEntity<Object> postItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                          @RequestHeader(value = "X-Sharer-User-Id",
                                                  required = true) Long requestorId) {
        return itemRequestClient.postItemRequest(itemRequestDto, requestorId);
    }

    /**
     * Получение запросов пользователя
     */
    @GetMapping
    public ResponseEntity<Object> getItemRequestsByRequestor(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long requestorId) {
        return itemRequestClient.getItemRequestsByRequestor(requestorId);
    }

    /**
     * Получение запроса по id
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader(value = "X-Sharer-User-Id",
                                                             required = true) Long requestorId) {
        return itemRequestClient.getItemRequestById(requestId, requestorId);
    }

    /**
     * Получение запросов вещей
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(@RequestParam(defaultValue = "0") Long from,
                                                  @RequestParam(defaultValue = "20") Long size,
                                                  @RequestHeader(value = "X-Sharer-User-Id",
                                                          required = true) Long requestorId) {
        return itemRequestClient.getItemRequests(from, size, requestorId);
    }
}