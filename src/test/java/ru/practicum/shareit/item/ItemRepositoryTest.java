package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByOwnerId() {
        Long ownerId = 1L;

        User owner = new User();
        owner.setId(ownerId);
        owner.setName("TestUser");
        owner.setEmail("testuser@user.com");
        userRepository.save(owner);

        Item item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Description1");
        item1.setAvailable(true);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Description2");
        item2.setAvailable(true);
        item2.setOwner(owner);

        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> result = itemRepository.findByOwnerId(ownerId, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).contains(item1, item2);
    }



    @Test
    void searchByText() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("TestUser");
        owner.setEmail("testuser@user.com");
        userRepository.save(owner);

        Item item1 = new Item();
        item1.setName("Apple");
        item1.setDescription("Delicious fruit");
        item1.setAvailable(true);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setName("Orange");
        item2.setDescription("Juicy fruit");
        item2.setAvailable(true);
        item2.setOwner(owner);

        Item item3 = new Item();
        item3.setName("Book");
        item3.setDescription("Interesting book");
        item3.setAvailable(true);
        item3.setOwner(owner);

        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(item3);
        entityManager.flush();

        String searchText = "fruit";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> result = itemRepository.searchByText(searchText, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).contains(item1, item2);
    }

}