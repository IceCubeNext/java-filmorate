package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    boolean containsFilm(Long id);

    Optional<Film> addFilm(Film film);

    Optional<Film> getFilmById(Long id);

    List<Film> getFilms();

    Optional<Film> updateFilm(Film film);

    Optional<Film> deleteFilm(Long id);
}
