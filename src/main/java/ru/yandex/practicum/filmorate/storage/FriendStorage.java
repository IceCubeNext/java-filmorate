package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    boolean addFriend(Long id, Long friendId);
    boolean deleteFriend(Long id, Long friendId);
    List<User> getFriends(Long id);
}
