package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO модель вещи.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotEmpty(message = "must not be empty")
    @Size(max = 50, message = "size must be between 0 and 50")
    private String name;

    @NotEmpty(message = "must not be empty")
    @Size(max = 200, message = "size must be between 0 and 200")
    private String description;

    @NotNull(message = "must not be null")
    private Boolean available;

    private Long requestId;
}