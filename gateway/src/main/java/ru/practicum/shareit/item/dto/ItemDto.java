package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO модель вещи.
 */
@Data
public class ItemDto {

    private Long id;

    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotEmpty
    @Size(max = 200)
    private String description;

    @NotNull
    private Boolean available;
}