package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {

    @Test
    public void testItem() {
        Long itemId = 1L;
        String name = "Item 1";
        String description = "This is item 1";
        Boolean available = true;

        User owner = new User(1L, "TestUser", "testuser@test.com");
        ItemRequest request = new ItemRequest(
                1L, "Item request 1", owner, LocalDateTime.now(), new ArrayList<>());

        Item item = new Item(itemId, name, description, available, owner, request);

        assertEquals(itemId, item.getId());
        assertEquals(name, item.getName());
        assertEquals(description, item.getDescription());
        assertEquals(available, item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(request, item.getRequest());
    }
}