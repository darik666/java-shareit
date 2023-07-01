package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.comment.DeniedCommentingException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UnauthorizedAccessException;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler = new ErrorHandler();

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.handleIllegalArgumentException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testHandleUserNotFound() {
        UserNotFoundException ex = new UserNotFoundException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.handleUserNotFound(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testHandleItemNotFound() {
        ItemNotFoundException ex = new ItemNotFoundException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.handleItemNotFound(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testUnauthorizedAccessException() {
        UnauthorizedAccessException ex = new UnauthorizedAccessException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.unauthorizedAccessException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testValidationNoSuchElementException() {
        NoSuchElementException ex = new NoSuchElementException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.validationNoSuchElementException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testEntityNotFoundExceptionException() {
        EntityNotFoundException ex = new EntityNotFoundException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.entityNotFoundExceptionException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testBookingNotFoundException() {
        BookingNotFoundException ex = new BookingNotFoundException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.bookingNotFoundException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testDeniedCommentingException() {
        DeniedCommentingException ex = new DeniedCommentingException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.deniedCommentingException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testUnHandledException() {
        Exception ex = new Exception("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.unHandledException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    public void testItemRequestNotFoundException() {
        ItemRequestNotFoundException ex = new ItemRequestNotFoundException("Test exception");
        String expectedErrorMessage = ex.getMessage();
        String actualErrorMessage = errorHandler.itemRequestNotFoundException(ex);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}