package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent(message = "Начало бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent(message = "Конец бронирования не может быть в прошлом")
    private LocalDateTime end;

    @NotNull(message = "itemId не может быть пустым")
    private Long itemId;
}