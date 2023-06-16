package ru.practicum.shareit.item.comment;

public class DeniedCommentingException extends RuntimeException {
    public DeniedCommentingException(String message) {
        super(message);
    }
}