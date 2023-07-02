package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemDtoWithBookingTest {

    @Test
    public void testItemDtoWithBookingValidation() {
        ItemDtoWithBooking itemDto = new ItemDtoWithBooking();
        itemDto.setId(1L);
        itemDto.setName("Item 1");
        itemDto.setDescription("This is item 1");
        itemDto.setAvailable(true);
        itemDto.setLastBooking(new BookingDtoForItem());
        itemDto.setNextBooking(new BookingDtoForItem());
        itemDto.setComments(new ArrayList<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemDtoWithBooking>> violations = validator.validate(itemDto);

        assertTrue(violations.isEmpty());
    }
}
