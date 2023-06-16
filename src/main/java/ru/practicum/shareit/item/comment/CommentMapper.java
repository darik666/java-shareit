package ru.practicum.shareit.item.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Маппер отзывов
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreateTime());
        commentDto.setText(comment.getText());
        return commentDto;
    }
}