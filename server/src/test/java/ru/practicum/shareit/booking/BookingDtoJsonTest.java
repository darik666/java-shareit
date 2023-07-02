package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(5);
        BookingDto bookingDto = new BookingDto(
                1L,
                start,
                end,
                1L);

        JsonContent<BookingDto> result = json.write(bookingDto);

        Assertions.assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

    @Test
    public void testBookingDtoNullValidation() {
        BookingDto bookingDto = new BookingDto(
                1L,
                null,
                null,
                null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        Assertions.assertThat(violations).hasSize(3);
        Assertions.assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("start")
                && violation.getMessage().equals("must not be null"));
        Assertions.assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("end")
                && violation.getMessage().equals("must not be null"));
        Assertions.assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("itemId")
                && violation.getMessage().equals("must not be null"));
    }
}