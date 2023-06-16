package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.comment.CommentDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDtoWithBooking {

    private Long id;

    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotEmpty
    @Size(max = 200)
    private String description;

    @NotNull
    private Boolean available;

    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;

    private List<CommentDto> comments = new ArrayList();
}
