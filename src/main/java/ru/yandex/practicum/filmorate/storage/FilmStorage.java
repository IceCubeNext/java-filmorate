package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    boolean containsFilm(long id);
    Optional<Film> addFilm(Film film);
    Optional<Film> getFilmById(long id);
    List<Film> getFilms();
    Optional<Film> updateFilm(Film film);
    Optional<Film> deleteFilm(long id);
    boolean addLike(long id, long userId);
    boolean deleteLike(long id, long userId);
}
