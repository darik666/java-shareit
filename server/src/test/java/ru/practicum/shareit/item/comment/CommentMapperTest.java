package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentMapperTest {

    @Test
    public void toCommentDto() {
        User user = new User(1L, "name", "name@email.com");
        ItemRequest itemRequest = new ItemRequest(
                1L, "desc", user, LocalDateTime.now(), new ArrayList<>());
        Item item = new Item(1L, "name", "descr", true, user, itemRequest);
        Comment comment = new Comment(1L, item, user, LocalDateTime.now(), "CommentText");

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
    }
}