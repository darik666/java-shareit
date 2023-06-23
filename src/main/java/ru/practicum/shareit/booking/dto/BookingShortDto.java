package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * DTO модель бронирования
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingShortDto {
    private Long id;
    private Item item;
    private User booker;
    private Status status;
}