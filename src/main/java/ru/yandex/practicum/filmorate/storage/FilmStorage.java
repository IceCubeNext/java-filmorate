package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    boolean isContainFilm(long id);
    Film addFilm(Film film);
    Film getFilmById(long id);
    List<Film> getFilms();
    Film updateFilm(Film film);
    Film deleteFilm(long id);
}
