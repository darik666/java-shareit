package ru.practicum.shareit.booking.dto;

import lombok.*;

/**
 * DTO модель бронирования
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoForItem {
    private Long id;
    private Long bookerId;
}