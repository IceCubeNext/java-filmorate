package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(long userId, long filmId) {
        if (!userStorage.containsUser(userId)) {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
        if (!filmStorage.containsFilm(filmId)) {
            throw new NotFoundException(String.format("Film with id=%d not found", filmId));
        }
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike(long userId, long filmId) {
        if (!userStorage.containsUser(userId)) {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
        if (!filmStorage.containsFilm(filmId)) {
            throw new NotFoundException(String.format("Film with id=%d not found", filmId));
        }
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
    }

    public List<Film> getTop(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size() * -1))
                .limit(count).collect(Collectors.toList());
    }
}
