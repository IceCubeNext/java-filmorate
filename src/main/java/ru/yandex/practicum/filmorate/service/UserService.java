package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDao") UserStorage userStorage,
                       @Qualifier("FriendDao") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public Optional<User> addUser(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public Optional<User> getUser(Long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public boolean addFriend(Long userId, Long friendId) {
        return friendStorage.addFriend(userId, friendId);
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        return friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        return friendStorage.getFriends(userId).stream()
                .filter(friendStorage.getFriends(friendId)::contains)
                .collect(Collectors.toList());
    }
}
