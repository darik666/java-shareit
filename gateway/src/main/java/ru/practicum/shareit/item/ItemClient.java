package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemById(long id, long ownerId) {
        return get("/" + id, ownerId);
    }

    public ResponseEntity<Object> postItem(ItemDto itemDto, long ownerId) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, long ownerId, long id) {
        return patch("/" + id, ownerId, itemDto);
    }

    public ResponseEntity<Object> deleteItem(Long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> searchItems(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> postComment(Long itemId, Comment comment, Long ownerId) {
        return  post("/" + itemId + "/comment", ownerId, comment);
    }

    public ResponseEntity<Object> searchComments(Long itemId, Long authorId, String text) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId,
                "authorId", authorId,
                "text", text
        );
        return get("/comments?state={state}&from={from}&size={size}", null, parameters);
    }
}