package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface LikeStorage {
    boolean addLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<Film> getUsersFavoriteFilms(Long id);

    List<User> getFilmFollowers(Long id);

    List<Film> getTop(Integer count);
}
