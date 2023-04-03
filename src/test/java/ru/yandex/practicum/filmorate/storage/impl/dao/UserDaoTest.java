package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDaoTest {
    private final UserDao userDao;

    @Test
    public void testContainsUser() {
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        assertTrue(userDao.containsUser(1));
        Exception exception = assertThrows(NotFoundException.class, () -> userDao.containsUser(2));
        String expectedMessage = "User with id=2 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testAddUser() {
        assertThrows(NotFoundException.class, () -> userDao.containsUser(1));
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        assertTrue(userDao.containsUser(1));
    }

    @Test
    public void testAddUserDuplicateLogin() {
        User user1 = User.builder()
                .name("User")
                .login("login")
                .email("user1@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User user2 = User.builder()
                .name("User")
                .login("login")
                .email("user2@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user1);
        assertTrue(userDao.containsUser(1));
        assertThrows(DuplicateKeyException.class, () -> userDao.addUser(user2));
    }

    @Test
    public void testAddUserDuplicateEmail() {
        User user1 = User.builder()
                .name("User")
                .login("login")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User user2 = User.builder()
                .name("User")
                .login("user")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user1);
        assertTrue(userDao.containsUser(1));
        assertThrows(DuplicateKeyException.class, () -> userDao.addUser(user2));
    }

    @Test
    public void testFindUserById() {
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        Optional<User> userOptional = userDao.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(usr ->
                        assertThat(usr).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetUsers() {
        User user1 = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user1);
        User user2 = User.builder()
                .name("User")
                .login("user")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user2);
        List<User> users = userDao.getUsers();
        assertEquals(2, users.size());
        assertEquals(1, users.get(0).getId());
        assertEquals(2, users.get(1).getId());
    }

    @Test
    public void testGetEmptyUsers() {
        List<User> users = userDao.getUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void testUpdateUserName() {
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        Optional<User> optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals("User", optionalUser.get().getName());

        user = optionalUser.get();
        user.setName("NewName");
        userDao.updateUser(user);
        optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals("NewName", optionalUser.get().getName());
    }

    @Test
    public void testUpdateUserLogin() {
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        Optional<User> optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals("login", optionalUser.get().getLogin());

        user = optionalUser.get();
        user.setLogin("NewLogin");
        userDao.updateUser(user);
        optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals("NewLogin", optionalUser.get().getLogin());
    }

    @Test
    public void testUpdateUserEmail() {
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        Optional<User> optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals("mail@mail.ru", optionalUser.get().getEmail());

        user = optionalUser.get();
        user.setEmail("newmail@mail.ru");
        userDao.updateUser(user);
        optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals("newmail@mail.ru", optionalUser.get().getEmail());
    }

    @Test
    public void testUpdateUserBirthday() {
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        Optional<User> optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals(LocalDate.of(1991, 12, 12), optionalUser.get().getBirthday());

        user = optionalUser.get();
        user.setBirthday(LocalDate.of(1991, 12, 1));
        userDao.updateUser(user);
        optionalUser = userDao.getUserById(1);
        assertTrue(optionalUser.isPresent());
        assertEquals(LocalDate.of(1991, 12, 1), optionalUser.get().getBirthday());
    }

    @Test
    public void testDeleteUser() {
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        userDao.addUser(user);
        assertTrue(userDao.containsUser(1));
        userDao.deleteUser(1);
        assertThrows(NotFoundException.class, () -> userDao.containsUser(1));
    }
}

