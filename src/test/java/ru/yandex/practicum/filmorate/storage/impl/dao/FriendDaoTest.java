package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FriendDaoTest {

    private final UserDao userDao;
    private final FriendDao friendDao;

    @Test
    public void testAddToFriends() {
        User user1 = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User user2 = User.builder()
                .name("User2")
                .login("login2")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1989, 10, 14))
                .build();
        Optional<User> optionalUser1 = userDao.addUser(user1);
        Optional<User> optionalUser2 = userDao.addUser(user2);
        assertTrue(optionalUser1.isPresent());
        assertTrue(optionalUser2.isPresent());
        long user1Id = optionalUser1.get().getId();
        long user2Id = optionalUser2.get().getId();
        friendDao.addFriend(user1Id, user2Id);
        assertEquals(1, friendDao.getFriends(user1Id).size());
        assertEquals(0, friendDao.getFriends(user2Id).size());
        friendDao.addFriend(user2Id, user1Id);
        assertEquals(1, friendDao.getFriends(user1Id).size());
        assertEquals(1, friendDao.getFriends(user2Id).size());
    }

    @Test
    public void testDeleteFromFriends() {
        User user1 = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User user2 = User.builder()
                .name("User2")
                .login("login2")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1989, 10, 14))
                .build();
        Optional<User> optionalUser1 = userDao.addUser(user1);
        Optional<User> optionalUser2 = userDao.addUser(user2);
        assertTrue(optionalUser1.isPresent());
        assertTrue(optionalUser2.isPresent());
        long user1Id = optionalUser1.get().getId();
        long user2Id = optionalUser2.get().getId();
        friendDao.addFriend(user1Id, user2Id);
        friendDao.addFriend(user2Id, user1Id);
        assertEquals(1, friendDao.getFriends(user1Id).size());
        assertEquals(1, friendDao.getFriends(user2Id).size());
        friendDao.deleteFriend(user1Id, user2Id);
        assertEquals(0, friendDao.getFriends(user1Id).size());
        assertEquals(1, friendDao.getFriends(user2Id).size());
        friendDao.deleteFriend(user2Id, user1Id);
        assertEquals(0, friendDao.getFriends(user1Id).size());
        assertEquals(0, friendDao.getFriends(user2Id).size());
    }

    @Test
    public void testGetFriends() {
        User user1 = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User user2 = User.builder()
                .name("User2")
                .login("login2")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1989, 10, 14))
                .build();
        Optional<User> optionalUser1 = userDao.addUser(user1);
        Optional<User> optionalUser2 = userDao.addUser(user2);
        assertTrue(optionalUser1.isPresent());
        assertTrue(optionalUser2.isPresent());
        long user1Id = optionalUser1.get().getId();
        long user2Id = optionalUser2.get().getId();
        friendDao.addFriend(user1Id, user2Id);
        friendDao.addFriend(user2Id, user1Id);
        assertEquals(List.of(optionalUser2.get()), friendDao.getFriends(user1Id));
        assertEquals(List.of(optionalUser1.get()), friendDao.getFriends(user2Id));
    }
}