package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if(StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User getUser(long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(long userId, long friendId) {
        if (!userStorage.containsUser(userId)) {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
        if (!userStorage.containsUser(friendId)) {
            throw new NotFoundException(String.format("User with id=%d not found", friendId));
        }
        userStorage.getUserById(userId).getFriendsId().add(friendId);
        userStorage.getUserById(friendId).getFriendsId().add(userId);
    }

    public void deleteFriend(long userId, long friendId) {
        if (!userStorage.containsUser(userId)) {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
        if (!userStorage.containsUser(friendId)) {
            throw new NotFoundException(String.format("User with id=%d not found", friendId));
        }
        userStorage.getUserById(userId).getFriendsId().remove(friendId);
        userStorage.getUserById(friendId).getFriendsId().remove(userId);
    }

    public List<User> getFriends(long userId) {
        return userStorage.getUserById(userId).getFriendsId().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getUserById(userId).getFriendsId().stream()
                .filter(userStorage.getUserById(friendId).getFriendsId()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
