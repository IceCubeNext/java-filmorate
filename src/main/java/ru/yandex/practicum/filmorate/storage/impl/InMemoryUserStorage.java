package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;
    public User addUser(User user) {
        int id = getNewId();
        user.setId(id);
        users.put(id, user);
        log.debug(String.format("Successfully added user %s", users.get(id)));
        return users.get(id);
    }

    public User getUserById(int id) {
        if(users.containsKey(id)) {
            return users.get(id);
        } else {
            log.error(String.format("User with id=%d not found", id));
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User updateUser(User user) {
        int id = user.getId();
        if(users.containsKey(id)) {
            users.put(id, user);
            return users.get(id);
        } else {
            log.error(String.format("User with id=%d not found", id));
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    public User deleteUser(int id) {
        if(users.containsKey(id)) {
            User user = users.get(id);
            users.remove(id);
            return user;
        } else {
            log.error(String.format("User with id=%d not found", id));
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }
    private int getNewId() {
        return id++;
    }
}
