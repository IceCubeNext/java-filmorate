package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        users.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.remove(user);
        users.add(user);
        return user;
    }
}
