package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(final IllegalArgumentException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String validationConstraintException(final ConstraintViolationException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String validationNoSuchElementException(final NoSuchElementException e) {
        log.debug("Получен статус 404 Not Found {}", e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String unHandledException(final Exception e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return e.getMessage();
    }
}