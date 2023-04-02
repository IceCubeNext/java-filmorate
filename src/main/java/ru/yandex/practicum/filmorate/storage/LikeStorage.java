package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface LikeStorage {
    boolean addLike(long id, long userId);
    boolean deleteLike(long id, long userId);
    List<Film> getUsersFavoriteFilms(long id);
    List<User> getFilmFollowers(long id);
    List<Film> getTop(int count);
}
