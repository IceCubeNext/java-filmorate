package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1L;

    @Override
    public boolean containsUser(long id) {
        return users.containsKey(id);
    }

    @Override
    public Optional<User> addUser(User user) {
        long id = getNewId();
        user.setId(id);
        users.put(id, user);
        return Optional.of(users.get(id));
    }

    @Override
    public Optional<User> getUserById(long id) {
        if(users.containsKey(id)) {
            return Optional.of(users.get(id));
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> updateUser(User user) {
        long id = user.getId();
        if(users.containsKey(id)) {
            users.put(id, user);
            return Optional.of(users.get(id));
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    @Override
    public Optional<User> deleteUser(long id) {
        if(users.containsKey(id)) {
            User user = users.get(id);
            users.remove(id);
            return Optional.of(user);
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    private long getNewId() {
        return id++;
    }
}
