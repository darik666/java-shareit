package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testCommentDto() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        CommentDto commentDto = new CommentDto(
                1L,
                "AuthorName",
                created,
                "text");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        String expectedCreated = created.format(formatter);

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("AuthorName");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(expectedCreated);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
    }

    @Test
    public void testCommentDtoBlankValidation() {
        LocalDateTime created = LocalDateTime.now();
        CommentDto commentDto = new CommentDto(
                1L,
                "AuthorName",
                created,
                "");


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertThat(violations).hasSize(1);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("text")
                && violation.getMessage().equals("must not be blank"));
    }

    @Test
    public void testCommentDtoSizeFormatValidation() {
        LocalDateTime created = LocalDateTime.now();
        CommentDto commentDto = new CommentDto(
                1L,
                "AuthorName",
                created,
                "textTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextText" +
                        "TextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextText" +
                        "TextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextText" +
                        "TextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextText" +
                        "TextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextText" +
                        "TextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextTextText" +
                        "TextT");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertThat(violations).hasSize(1);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("text")
                && violation.getMessage().equals("size must be between 0 and 500"));
    }
}