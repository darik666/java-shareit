package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO модель вещи.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotEmpty
    @Size(max = 200)
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private Integer request;
}