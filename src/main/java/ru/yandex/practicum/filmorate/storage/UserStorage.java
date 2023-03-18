package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    boolean containsUser(long id);
    User addUser(User user);
    User getUserById(long id);
    List<User> getUsers();
    User updateUser(User user);
    User deleteUser(long id);
    User addFriend(long id, long friendId);
    User deleteFriend(long id, long friendId);
}
