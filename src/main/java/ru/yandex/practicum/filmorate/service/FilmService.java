package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDao") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
}
