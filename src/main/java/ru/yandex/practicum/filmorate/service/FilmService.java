package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDao") FilmStorage filmStorage,
                       @Qualifier("UserDao") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Optional<Film> addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Optional<Film> getFilm(long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public boolean addLike(long userId, long filmId) {
        if (userStorage.containsUser(userId)) {
            return filmStorage.addLike(filmId, userId);
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
    }

    public boolean deleteLike(long userId, long filmId) {
        if (userStorage.containsUser(userId)) {
            return filmStorage.deleteLike(filmId, userId);
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        }
    }

    public List<Film> getTop(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparing(f -> f.getLikes().size() * -1))
                .limit(count).collect(Collectors.toList());
    }
}
