package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dao.ItemMemoryDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UnauthorizedAccessException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserMemoryDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@SpringBootTest
class ShareItTests {
	private UserController userController;
	private ItemController itemController;

	@BeforeEach
	void setup() {
		UserDao userMemoryDao = new UserMemoryDao();
		userController = new UserController(new UserServiceImpl(userMemoryDao));
		itemController = new ItemController(new ItemServiceImpl(new ItemMemoryDao(), userMemoryDao));
	}

	UserDto createUser1() {
		return userController.postUser(new UserDto(null, "user", "user@user.com"));
	}

	ItemDto createItem1() {
		userController.postUser(new UserDto(null, "user", "user@user.com"));
		ItemDto itemDto = new ItemDto(null, "Дрель",
				"Простая дрель", true, null, null);
		ItemDto createdItem = itemController.postItem(itemDto, 1);
		return createdItem;
	}

	@Test
	void createUser_Successful() {
		UserDto createdUser = createUser1();

		assertEquals("user", createdUser.getName());
		assertEquals("user@user.com", createdUser.getEmail());
	}

	@Test
	void getAllUsers() {
		createUser1();

		List<UserDto> userList = userController.getUsers();

		assertEquals(1, userList.size());
		assertEquals(1, userList.get(0).getId());
	}

	@Test
	void createUser_FailNoEmail() {
		UserDto userDto = new UserDto(null, "user", null);

		assertThrows(IllegalArgumentException.class, () -> userController.postUser(userDto));
	}

	@Test
	void createUser_ExistingEmail() {
		createUser1();

		assertThrows(EmailExistsException.class,
				() -> userController.postUser(new UserDto(null, "user", "user@user.com")));
	}

	@Test
	void getUserById_ExistingId() {
		createUser1();

		UserDto existingUser = userController.getUserById(1);

		assertEquals("user", existingUser.getName());
		assertEquals("user@user.com", existingUser.getEmail());
	}

	@Test
	void getUserById_NonExistingId() {
		assertThrows(UserNotFoundException.class, () -> userController.getUserById(100));
	}

	@Test
	void updateUser_ExistingUser() {
		createUser1();
		UserDto updatedUser = new UserDto(null, "updatedUser", "updatedUser@user.com");

		UserDto existingUser = userController.updateUser(updatedUser, 1);

		assertEquals("updatedUser", existingUser.getName());
		assertEquals("updatedUser@user.com", existingUser.getEmail());
	}

	@Test
	void updateUser_NonExistingUser() {
		UserDto nonExistingUser = new UserDto(100, "nonExistingUser", "nonExistingUser@user.com");

		assertThrows(UserNotFoundException.class,
				() -> userController.updateUser(nonExistingUser, nonExistingUser.getId()));
	}

	@Test
	void updateUser_ExistingEmail() {
		createUser1();
		userController.postUser(new UserDto(null, "user", "user2@user2.com"));
		UserDto existingEmailUser = new UserDto(1, "user", "user2@user2.com");

		assertThrows(EmailExistsException.class,
				() -> userController.updateUser(existingEmailUser, existingEmailUser.getId()));
	}

	@Test
	void deleteUser_ExistingId() {
		createUser1();

		userController.deleteUser(1);

		assertThrows(UserNotFoundException.class, () -> userController.getUserById(1));
	}

	@Test
	void deleteUser_NonExistingId() {
		assertThrows(UserNotFoundException.class, () -> userController.deleteUser(100));
	}

	@Test
	void createItem_Successful() {
		ItemDto item = createItem1();

		assertEquals("Дрель", item.getName());
		assertEquals("Простая дрель", item.getDescription());
	}

	@Test
	void getItems() {
		createItem1();
		ItemDto itemDto = new ItemDto(null, "Тролл",
				"Простой тролл", true, null, null);
		itemController.postItem(itemDto, 1);

		List<ItemDto> itemsList = itemController.getItems(1);

		assertEquals(2, itemsList.size());
	}

	@Test
	void getItemById() {
		createItem1();

		ItemDto item = itemController.getItemById(1);

		assertEquals(1, item.getId());
		assertEquals("Простая дрель", item.getDescription());
	}

	@Test
	void getItemByIdNotFound() {
		assertThrows(ItemNotFoundException.class,
				() -> itemController.getItemById(2));
	}

	@Test
	void updateItem_Successful() {
		createItem1();
		ItemDto updatedItem = itemController.updateItem(new ItemDto(null, "Дреллер",
				"Простейший дреллер", false, null, null), 1, 1);

		assertEquals("Простейший дреллер", updatedItem.getDescription());
		assertEquals(false, updatedItem.getAvailable());
	}

	@Test
	void updateItem_ItemNotFound() {
		createItem1();

		ItemDto updatedItem = new ItemDto(null, "Дреллер",
				"Простейший дреллер", false, null, null);

		assertThrows(ItemNotFoundException.class,
				() -> itemController.updateItem(updatedItem, 2, 1));
	}

	@Test
	void updateItem_UnauthorizedAccess() {
		createItem1();
		UserDto userDto = new UserDto(null, "user2", "user2@user2.com");
		userController.postUser(userDto);

		ItemDto updatedItem = new ItemDto(null, "Дреллер",
				"Простейший дреллер", false, null, null);

		assertThrows(UnauthorizedAccessException.class,
				() -> itemController.updateItem(updatedItem, 1, 2));
	}

	@Test
	void deleteItemNotFound() {
		createItem1();

		assertThrows(ItemNotFoundException.class, () -> itemController.deleteItem(2));
	}

	@Test
	void deleteItemSuccess() {
		createItem1();
		itemController.deleteItem(1);
		List<ItemDto> itemsList = itemController.getItems(1);

		assertEquals(true, itemsList.isEmpty());
	}

	@Test
	void searchItems() {
		createItem1();
		ItemDto itemDto = new ItemDto(null, "СуперД",
				"просто дреллер", true, null, null);
		itemController.postItem(itemDto, 1);

		List<ItemDto> itemList = itemController.searchItems("Дрел");

		assertEquals(2, itemList.size());
		assertEquals(true, itemController.searchItems("").isEmpty());
	}
}