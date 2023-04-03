package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    boolean containsUser(Long id);
    Optional<User> addUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getUsers();
    Optional<User> updateUser(User user);
    Optional<User> deleteUser(Long id);
}
