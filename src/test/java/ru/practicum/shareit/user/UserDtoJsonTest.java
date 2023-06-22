package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(
                1L,
                "TestUser",
                "testuser@test.com");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("TestUser");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("testuser@test.com");
    }

    @Test
    void testUserDtoBadEmailValidation() {
        UserDto userDto = new UserDto(
                1L,
                "TestUser",
                "badEmail");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto, UserDto.OnCreate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                && violation.getMessage().equals("должно иметь формат адреса электронной почты"));
    }

    @Test
    void testUserDtoBlankEmailValidation() {
        UserDto userDto = new UserDto(
                1L,
                "TestUser",
                "");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto, UserDto.OnCreate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email")
                && violation.getMessage().equals("не должно быть пустым"));
    }
}