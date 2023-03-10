package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    public void clearData() {
        users.clear();
        id = 0;
    }


    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        int id = getNewId();
        user.setId(id);
        users.put(id, user);
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        log.debug("create new user " + user);
        return ResponseEntity.ok(user).getBody();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.debug("user " + users.get(user.getId()) + " change data to " + user);
            users.put(user.getId(), user);
        } else {
            log.error("error while updating: user with id=" + user.getId() + " not found");
            throw new NotFoundException("error while updating: user with id=" + user.getId() + " not found");
        }
        return ResponseEntity.ok(user).getBody();
    }

    private int getNewId() {
        return ++id;
    }
}
