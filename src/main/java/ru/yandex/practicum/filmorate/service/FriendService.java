package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FriendService {
    private final FriendStorage friendStorage;

    @Autowired
    public FriendService(@Qualifier("FriendDao") FriendStorage friendStorage) {
        this.friendStorage = friendStorage;
    }

    public boolean addFriend(long userId, long friendId) {
        return friendStorage.addFriend(userId, friendId);
    }

    public boolean deleteFriend(long userId, long friendId) {
        return friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return friendStorage.getFriends(userId).stream()
                .filter(friendStorage.getFriends(friendId)::contains)
                .collect(Collectors.toList());
    }
}
