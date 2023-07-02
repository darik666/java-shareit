package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO модель запроса вещей
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "must not be blank")
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
    private List<ItemDto> items;
}