package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDao") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> addUser(User user) {
        if(StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public Optional<User> getUser(long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public boolean addFriend(long userId, long friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public boolean deleteFriend(long userId, long friendId) {
        return userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        if (userStorage.getUserById(userId).isEmpty()) {
            return Collections.emptyList();
        } else {
            return userStorage.getUserById(userId).get().getFriendsId().stream()
                    .map(userStorage::getUserById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        if (userStorage.getUserById(userId).isEmpty() || userStorage.getUserById(friendId).isEmpty()) {
            return Collections.emptyList();
        } else {
            return userStorage.getUserById(userId).get().getFriendsId().stream()
                    .filter(userStorage.getUserById(friendId).get().getFriendsId()::contains)
                    .map(userStorage::getUserById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
    }
}
