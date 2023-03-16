package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable Long id) {
        log.debug(String.format("Get user with id=%d", id));
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.debug(String.format("Get users. Return %d items", userService.getUsers().size()));
        return userService.getUsers();
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.debug(String.format("Get: friends of user with id=%d. Return %d items", id, userService.getUsers().size()));
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getFriends(@PathVariable Long id,
                                 @PathVariable Long otherId) {
        log.debug(String.format("Get: common friends of users with id=%d and id=%d. Return %d items", id, otherId, userService.getUsers().size()));
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        log.debug(String.format("Post new user %s", user));
        userService.addUser(user);
        return ResponseEntity.ok(user).getBody();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug(String.format("Put user %s", user));
        userService.updateUser(user);
        return ResponseEntity.ok(user).getBody();
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        log.debug(String.format("Put: user id=%d add friend with id=%d", id, friendId));
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        log.debug(String.format("Put: user id=%d delete friend with id=%d", id, friendId));
        userService.deleteFriend(id, friendId);
    }
}
