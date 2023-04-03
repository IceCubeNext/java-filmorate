package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryFriendStorage")
public class InMemoryFriendStorage implements FriendStorage {
    UserStorage userStorage;
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    public InMemoryFriendStorage(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
            setFriend(id, friendId);
            setFriend(friendId, id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        if (userStorage.containsUser(id) && userStorage.containsUser(friendId)) {
            removeFriend(id, friendId);
            removeFriend(friendId, id);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getFriends(Long id) {
        if (userStorage.containsUser(id)) {
            if (friends.containsKey(id)) {
                return friends.get(id).stream()
                        .map(userStorage::getUserById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public void removeUser(Long id) {
        if (friends.containsKey(id)) {
            for (long friendId : friends.get(id)) {
                if (friends.containsKey(friendId)) {
                    friends.get(friendId).remove(id);
                    if (friends.get(friendId).size() == 0) friends.remove(friendId);
                }
            }
            friends.remove(id);
        }
    }

    private void removeFriend(Long id, Long friendId) {
        if (friends.containsKey(id)) {
            friends.get(id).remove(friendId);
            if (friends.get(id).size() == 0) {
                friends.remove(id);
            }
        }
    }

    private void setFriend(Long id, Long friendId) {
        if (friends.containsKey(id)) {
            friends.get(id).add(friendId);
        } else {
            friends.put(id, new HashSet<>());
            friends.get(id).add(friendId);
        }
    }
}
