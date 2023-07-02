package ru.practicum.shareit.item.comment;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Класс модели отзывов
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Comment {
    private Long id;

    private ItemDto item;

    private UserDto author;

    private LocalDateTime createTime;

    @NotBlank
    @Size(max = 2000)
    private String text;
}