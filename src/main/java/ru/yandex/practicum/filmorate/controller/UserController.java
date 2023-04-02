package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FriendService friendService;
    @Autowired
    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping("{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        log.debug(String.format("Get: get user with id=%d", id));
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.debug(String.format("Get: get users. Return %d items", userService.getUsers().size()));
        return userService.getUsers();
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.debug(String.format("Get: get friends of user with id=%d.", id));
        return friendService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getFriends(@PathVariable Long id,
                                 @PathVariable Long otherId) {
        log.debug(String.format("Get: get common friends of users with id=%d and id=%d.", id, otherId));
        return friendService.getCommonFriends(id, otherId);
    }

    @PostMapping()
    public Optional<User> addUser(@Valid @RequestBody User user) {
        log.debug(String.format("Post: add new user %s", user));
        return userService.addUser(user);
    }

    @PutMapping
    public Optional<User> updateUser(@Valid @RequestBody User user) {
        log.debug(String.format("Put: update for user %s", user));
        return userService.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        log.debug(String.format("Put: user id=%d add friend with id=%d", id, friendId));
        return friendService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public boolean deleteFriend(@PathVariable Long id,
                                @PathVariable Long friendId) {
        log.debug(String.format("Put: user id=%d delete friend with id=%d", id, friendId));
        return friendService.deleteFriend(id, friendId);
    }
}
