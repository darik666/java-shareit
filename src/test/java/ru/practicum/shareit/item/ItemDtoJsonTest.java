package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(
                1L,
                "ItemName",
                "ItemDesc",
                true,
                1L);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("ItemDesc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    public void testItemDtoNullEmptyValidation() {
        ItemDto itemDto = new ItemDto(
                1L,
                "",
                "",
                null,
                1L);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(3);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("available")
                && violation.getMessage().equals("must not be null"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                && violation.getMessage().equals("must not be empty"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("description")
                && violation.getMessage().equals("must not be empty"));
    }

    @Test
    public void testItemDtoSizeValidation() {
        ItemDto itemDto = new ItemDto(
                1L,
                "ItemNameItemNameItemNameItemNameItemNameItemNameItemName",
                "ItemDescriptionItemDescriptionItemDescriptionItemDescriptionItemDescription" +
                        "ItemDescriptionItemDescriptionItemDescriptionItemDescriptionItemDescription" +
                        "ItemDescriptionItemDescriptionItemDescriptionItemDescription",
                true,
                1L);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        assertThat(violations).hasSize(2);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("name")
                && violation.getMessage().equals("size must be between 0 and 50"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("description")
                && violation.getMessage().equals("size must be between 0 and 200"));
    }
}