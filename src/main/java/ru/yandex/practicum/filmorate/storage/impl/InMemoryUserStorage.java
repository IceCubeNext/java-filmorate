package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1L;

    public boolean isContainUser(long id) {
        return users.containsKey(id);
    }

    public User addUser(User user) {
        long id = getNewId();
        user.setId(id);
        if (user.getFriendsId() == null) {
            user.setFriendsId(new HashSet<>());
        }
        users.put(id, user);
        log.debug(String.format("Successfully added user %s", users.get(id)));
        return users.get(id);
    }

    public User getUserById(long id) {
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
        long id = user.getId();
        if(users.containsKey(id)) {
            if (user.getFriendsId() == null) {
                user.setFriendsId(new HashSet<>());
            }
            users.put(id, user);
            return users.get(id);
        } else {
            log.error(String.format("User with id=%d not found", id));
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    public User deleteUser(long id) {
        if(users.containsKey(id)) {
            User user = users.get(id);
            users.remove(id);
            return user;
        } else {
            log.error(String.format("User with id=%d not found", id));
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }
    private long getNewId() {
        return id++;
    }
}
