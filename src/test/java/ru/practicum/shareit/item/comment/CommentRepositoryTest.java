package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager entityManager;

    User owner;
    Item item;

    void createUserAndItem() {
        owner = new User();
        owner.setId(1L);
        owner.setName("TestUser");
        owner.setEmail("testuser@user.com");
        userRepository.save(owner);

        item = new Item();
        item.setName("Item1");
        item.setOwner(owner);
        item.setDescription("Description1");

        itemRepository.save(item);
    }

    @Test
    void findByItemId() {
        createUserAndItem();

        Comment comment1 = new Comment();
        comment1.setItem(item);
        comment1.setText("Comment1");
        comment1.setAuthor(owner);
        comment1.setCreateTime(LocalDateTime.now());
        entityManager.persist(comment1);

        Comment comment2 = new Comment();
        comment2.setItem(item);
        comment2.setText("Comment2");
        comment2.setAuthor(owner);
        comment2.setCreateTime(LocalDateTime.now());
        entityManager.persist(comment2);

        entityManager.flush();

        List<Comment> result = commentRepository.findByItemId(item.getId());

        assertThat(result).isNotEmpty();
        assertThat(result).contains(comment1, comment2);
    }


    @Test
    void findByAuthorId() {
        createUserAndItem();

        Comment comment1 = new Comment();
        comment1.setItem(item);
        comment1.setText("Comment1");
        comment1.setAuthor(owner);
        comment1.setCreateTime(LocalDateTime.now());
        entityManager.persist(comment1);

        Comment comment2 = new Comment();
        comment2.setItem(item);
        comment2.setText("Comment2");
        comment2.setAuthor(owner);
        comment2.setCreateTime(LocalDateTime.now());
        entityManager.persist(comment2);

        entityManager.flush();

        List<Comment> result = commentRepository.findByAuthorId(owner.getId());

        assertThat(result).isNotEmpty();
        assertThat(result).contains(comment1, comment2);
    }

    @Test
    void findByTextContainingIgnoreCase() {
        createUserAndItem();

        String searchText = "example";

        Comment comment1 = new Comment();
        comment1.setText("This is an example comment");
        comment1.setItem(item);
        comment1.setAuthor(owner);
        comment1.setCreateTime(LocalDateTime.now());
        entityManager.persist(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Another example comment");
        comment2.setItem(item);
        comment2.setAuthor(owner);
        comment2.setCreateTime(LocalDateTime.now());
        entityManager.persist(comment2);

        Comment comment3 = new Comment();
        comment3.setItem(item);
        comment3.setText("This comment is not relevant");
        comment3.setAuthor(owner);
        comment3.setCreateTime(LocalDateTime.now());
        entityManager.persist(comment3);

        entityManager.flush();

        List<Comment> result = commentRepository.findByTextContainingIgnoreCase(searchText);

        assertThat(result).isNotEmpty();
        assertThat(result).contains(comment1, comment2);
        assertThat(result).doesNotContain(comment3);
    }
}