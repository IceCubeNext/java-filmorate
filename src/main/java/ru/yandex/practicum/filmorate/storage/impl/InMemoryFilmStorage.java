package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    public Film addFilm(Film film) {
        int id = getNewId();
        film.setId(id);
        films.put(id, film);
        log.debug(String.format("Successfully added film %s", films.get(id)));
        return films.get(id);
    }

    public Film getFilmById(int id) {
        if(films.containsKey(id)) {
            return films.get(id);
        } else {
            log.error(String.format("Film with id=%d not found", id));
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film updateFilm(Film film) {
        int id = film.getId();
        if(films.containsKey(id)) {
            films.put(id, film);
            return films.get(id);
        } else {
            log.error(String.format("Film with id=%d not found", id));
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    public Film deleteFilm(int id) {
        if(films.containsKey(id)) {
            Film film = films.get(id);
            films.remove(id);
            return film;
        } else {
            log.error(String.format("Film with id=%d not found", id));
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    private int getNewId() {
        return id++;
    }
}
