package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    boolean containsUser(long id);
    Optional<User> addUser(User user);
    Optional<User> getUserById(long id);
    List<User> getUsers();
    Optional<User> updateUser(User user);
    Optional<User> deleteUser(long id);
    boolean addFriend(long id, long friendId);
    boolean deleteFriend(long id, long friendId);
}
