package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    boolean addFriend(long id, long friendId);
    boolean deleteFriend(long id, long friendId);
    List<User> getFriends(long id);
}
