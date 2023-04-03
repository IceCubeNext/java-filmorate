package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Slf4j
@Service
public class LikeService {
    private final LikeStorage likeStorage;

    @Autowired
    public LikeService(@Qualifier("LikeDao") LikeStorage likeStorage) {
        this.likeStorage = likeStorage;
    }

    public boolean addLike(Long userId, Long filmId) {
        return likeStorage.addLike(filmId, userId);
    }

    public boolean deleteLike(Long userId, Long filmId) {
        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getTop(int count) {
        return likeStorage.getTop(count);
    }

    public List<Film> getUsersFavoriteFilms(Long id) {
        return likeStorage.getUsersFavoriteFilms(id);
    }

    public List<User> getFilmFollowers(Long id) {
        return likeStorage.getFilmFollowers(id);
    }
}
