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

    @GetMapping
    public List<User> getUsers() {
        log.debug(String.format("Get users. Return %d items", userService.getUsers().size()));
        return userService.getUsers();
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


}
