package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentTest {

    @Test
    public void testComment() {
        Item item = new Item();
        item.setId(1L);
        User user = new User(1L, "name", "name@email.com");
        LocalDateTime created = LocalDateTime.now();

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreateTime(created);
        comment.setText("Comment");

        assertEquals(1L, comment.getId());
        assertEquals(item, comment.getItem());
        assertEquals(user.getId(), comment.getAuthor().getId());
        assertEquals(created, comment.getCreateTime());
        assertEquals("Comment", comment.getText());
    }
}
