package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto = new ItemDto(1L, "Name1", "Desc1", true, 1L);
    private ItemDto itemDto2 = new ItemDto(2L, "Name2", "Desc2", true, 1L);

    @Test
    public void getItemsTest() throws Exception {
        ItemDtoWithBooking itemDtoWithBooking1 = new ItemDtoWithBooking(1L, "Item1", "Desc1",
                true, null, null, null);
        ItemDtoWithBooking itemDtoWithBooking2 = new ItemDtoWithBooking(2L, "Item2", "Desc2",
                true, null, null, null);
        List<ItemDtoWithBooking> itemList = List.of(itemDtoWithBooking1, itemDtoWithBooking2);

        when(itemService.getItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemList);

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Item1"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Desc2"))
                .andExpect(jsonPath("$[1].name").value("Item2"));
    }

    @Test
    public void getItemTest() throws Exception {
        ItemDtoWithBooking itemDtoWithBooking = new ItemDtoWithBooking(1L, "Item1", "ItemDesc",
                true, null, null, null);
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDtoWithBooking);

        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Item1"))
                .andExpect(jsonPath("$.description").value("ItemDesc"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void postItemTest() throws Exception {
        when(itemService.postItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"name\":\"Дрель\",\"description\":\"Простая дрель\",\"available\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Desc1"))
                .andExpect(jsonPath("$.name").value("Name1"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.requestId").value(1));
    }

    @Test
    public void updateItemTest() throws Exception {
        when(itemService.updateItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"name\": \"Item 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Desc1"))
                .andExpect(jsonPath("$.name").value("Name1"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.requestId").value(1));
    }

    @Test
    public void deleteItemTest() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchItemsTest() throws Exception {
        List<ItemDto> itemList = List.of(itemDto, itemDto2);

        when(itemService.searchItems(anyString(), anyInt(), anyInt()))
                .thenReturn(itemList);

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "search text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Name1"))
                .andExpect(jsonPath("$[0].description").value("Desc1"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].requestId").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Name2"))
                .andExpect(jsonPath("$[1].description").value("Desc2"))
                .andExpect(jsonPath("$[1].available").value(true))
                .andExpect(jsonPath("$[1].requestId").value(1));
    }

    @Test
    public void addCommentTest() throws Exception {
        LocalDateTime nowTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        CommentDto commentDto = new CommentDto(1L, "Author", nowTime, "MyComment");

        when(itemService.postComment(anyLong(), any(Comment.class), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"text\":\"Comment for item 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("MyComment"))
                .andExpect(jsonPath("$.authorName").value("Author"))
                .andExpect(jsonPath("$.created").value(nowTime.format(formatter)));
    }

    @Test
    public void searchCommentsTest() throws Exception {
        CommentDto comment1 = new CommentDto(1L, "Author1", LocalDateTime.now(), "Comment1");
        CommentDto comment2 = new CommentDto(2L, "Author2", LocalDateTime.now(), "Comment2");
        List<CommentDto> comments = List.of(comment1, comment2);

        when(itemService.searchComments(anyLong(), anyLong(), anyString())).thenReturn(comments);

        mockMvc.perform(get("/items/comments")
                        .param("itemId", "123")
                        .param("authorId", "456")
                        .param("text", "example"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].authorName").value("Author1"))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].text").value("Comment1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].authorName").value("Author2"))
                .andExpect(jsonPath("$[1].created").exists())
                .andExpect(jsonPath("$[1].text").value("Comment2"));

        verify(itemService).searchComments(123L, 456L, "example");
    }
}