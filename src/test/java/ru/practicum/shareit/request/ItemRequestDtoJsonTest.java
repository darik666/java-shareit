package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        User user = new User(1L, "N", "n@n.com");
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L,
                "Descr",
                user,
                created,
                new ArrayList<>());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        String expectedCreated = created.format(formatter);

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Descr");
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.requestor.name").isEqualTo("N");
        assertThat(result).extractingJsonPathStringValue("$.requestor.email").isEqualTo("n@n.com");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEmpty();
    }

    @Test
    public void testItemRequestDtoBlankValidation() {
        LocalDateTime created = LocalDateTime.now();
        User user = new User(1L, "N", "n@n.com");
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                1L,
                "",
                user,
                created,
                new ArrayList<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("description")
                && violation.getMessage().equals("не должно быть пустым"));
    }
}