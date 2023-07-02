package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestTest {

    @Test
    public void testItemRequestCreation() {
        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("TestUser");

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        items.add(item1);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item2");
        items.add(item2);

        LocalDateTime created = LocalDateTime.now();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request description");
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(created);
        itemRequest.setItems(items);

        assertEquals(1L, itemRequest.getId());
        assertEquals("Request description", itemRequest.getDescription());
        assertEquals(requestor, itemRequest.getRequestor());
        assertEquals(created, itemRequest.getCreated());
        assertEquals(items, itemRequest.getItems());
    }
}