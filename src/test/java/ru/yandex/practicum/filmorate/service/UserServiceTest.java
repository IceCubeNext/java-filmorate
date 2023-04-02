//package ru.yandex.practicum.filmorate.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//class UserServiceTest {
//    @Autowired
//    UserService userService;
//
//    @Test
//    public void addFriendsNormal() {
//        User user = User.builder()
//                .name("Alex")
//                .login("login")
//                .email("mail@mail.ru")
//                .birthday(LocalDate.of(1991, 12, 12))
//                .build();
//        User oneUser = userService.addUser(user);
//        user = User.builder()
//                .name("Oleg")
//                .login("oleg")
//                .email("oleg@mail.ru")
//                .birthday(LocalDate.of(1991, 10, 1))
//                .build();
//        User twoUser = userService.addUser(user);
//        assertEquals(0, oneUser.getFriendsId().size());
//        assertEquals(0, twoUser.getFriendsId().size());
//
//        userService.addFriend(oneUser.getId(), twoUser.getId());
//        assertEquals(1, oneUser.getFriendsId().size());
//        assertEquals(1, twoUser.getFriendsId().size());
//        assertTrue(twoUser.getFriendsId().contains(oneUser.getId()));
//        assertTrue(oneUser.getFriendsId().contains(twoUser.getId()));
//    }
//
//    @Test
//    public void addNonexistentFriends() {
//        User user = User.builder()
//                .name("Alex")
//                .login("login")
//                .email("mail@mail.ru")
//                .birthday(LocalDate.of(1991, 12, 12))
//                .build();
//        User oneUser = userService.addUser(user);
//        Exception exception = assertThrows(NotFoundException.class, () -> userService.addFriend(oneUser.getId(), 10));
//        String expectedMessage = "User with id=10 not found";
//        String actualMessage = exception.getMessage();
//        assertEquals(actualMessage, expectedMessage);
//    }
//
//    @Test
//    public void deleteFriendsNormal() {
//        User user = User.builder()
//                .name("Alex")
//                .login("login")
//                .email("mail@mail.ru")
//                .birthday(LocalDate.of(1991, 12, 12))
//                .build();
//        User oneUser = userService.addUser(user);
//        user = User.builder()
//                .name("Oleg")
//                .login("oleg")
//                .email("oleg@mail.ru")
//                .birthday(LocalDate.of(1991, 10, 1))
//                .build();
//        User twoUser = userService.addUser(user);
//        userService.addFriend(oneUser.getId(), twoUser.getId());
//        assertEquals(1, oneUser.getFriendsId().size());
//        assertEquals(1, twoUser.getFriendsId().size());
//        assertTrue(twoUser.getFriendsId().contains(oneUser.getId()));
//        assertTrue(oneUser.getFriendsId().contains(twoUser.getId()));
//
//        userService.deleteFriend(oneUser.getId(), twoUser.getId());
//        assertEquals(0, oneUser.getFriendsId().size());
//        assertEquals(0, twoUser.getFriendsId().size());
//    }
//
//    @Test
//    public void deleteNonexistentFriends() {
//        User user = User.builder()
//                .name("Alex")
//                .login("login")
//                .email("mail@mail.ru")
//                .birthday(LocalDate.of(1991, 12, 12))
//                .build();
//        User oneUser = userService.addUser(user);
//        Exception exception = assertThrows(NotFoundException.class, () -> userService.deleteFriend(oneUser.getId(), 10));
//        String expectedMessage = "User with id=10 not found";
//        String actualMessage = exception.getMessage();
//        assertEquals(actualMessage, expectedMessage);
//    }
//
//    @Test
//    public void getFriendsNormal() {
//        User user = User.builder()
//                .name("Alex")
//                .login("login")
//                .email("mail@mail.ru")
//                .birthday(LocalDate.of(1991, 12, 12))
//                .build();
//        User oneUser = userService.addUser(user);
//        user = User.builder()
//                .name("Oleg")
//                .login("oleg")
//                .email("oleg@mail.ru")
//                .birthday(LocalDate.of(1991, 10, 1))
//                .build();
//        User twoUser = userService.addUser(user);
//        user = User.builder()
//                .name("Georg")
//                .login("georg")
//                .email("georg@mail.ru")
//                .birthday(LocalDate.of(1910, 10, 1))
//                .build();
//        User threeUser = userService.addUser(user);
//        userService.addFriend(oneUser.getId(), twoUser.getId());
//        userService.addFriend(oneUser.getId(), threeUser.getId());
//        assertEquals(2, oneUser.getFriendsId().size());
//        assertEquals(1, twoUser.getFriendsId().size());
//        assertEquals(1, threeUser.getFriendsId().size());
//        assertEquals(List.of(twoUser, threeUser), userService.getFriends(oneUser.getId()));
//    }
//
//    @Test
//    public void getCommonFriendsNormal() {
//        User user = User.builder()
//                .name("Alex")
//                .login("login")
//                .email("mail@mail.ru")
//                .birthday(LocalDate.of(1991, 12, 12))
//                .build();
//        User oneUser = userService.addUser(user);
//        user = User.builder()
//                .name("Oleg")
//                .login("oleg")
//                .email("oleg@mail.ru")
//                .birthday(LocalDate.of(1991, 10, 1))
//                .build();
//        User twoUser = userService.addUser(user);
//        user = User.builder()
//                .name("Georg")
//                .login("georg")
//                .email("georg@mail.ru")
//                .birthday(LocalDate.of(1910, 10, 1))
//                .build();
//        User threeUser = userService.addUser(user);
//        userService.addFriend(oneUser.getId(), twoUser.getId());
//        userService.addFriend(threeUser.getId(), twoUser.getId());
//        assertEquals(1, oneUser.getFriendsId().size());
//        assertEquals(2, twoUser.getFriendsId().size());
//        assertEquals(1, threeUser.getFriendsId().size());
//        assertEquals(List.of(twoUser), userService.getCommonFriends(oneUser.getId(), threeUser.getId()));
//    }
//}