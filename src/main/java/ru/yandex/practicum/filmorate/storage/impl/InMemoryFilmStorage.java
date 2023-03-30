package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1L;

    public boolean containsFilm(long id) {
        return films.containsKey(id);
    }

    public Optional<Film> addFilm(Film film) {
        long id = getNewId();
        film.setId(id);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(id, film);
        return Optional.of(films.get(id));
    }

    public Optional<Film> getFilmById(long id) {
        if(films.containsKey(id)) {
            return Optional.of(films.get(id));
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Optional<Film> updateFilm(Film film) {
        long id = film.getId();
        if(films.containsKey(id)) {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            films.put(id, film);
            return Optional.of(films.get(id));
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    public Optional<Film> deleteFilm(long id) {
        if(films.containsKey(id)) {
            Film film = films.get(id);
            films.remove(id);
            return Optional.of(film);
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    public boolean addLike(long id, long userId) {
        if (films.containsKey(id)) {
            films.get(id).getLikes().add(userId);
            return true;
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    public boolean deleteLike(long id, long userId) {
        if (films.containsKey(id)) {
            films.get(id).getLikes().remove(userId);
            return true;
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    private long getNewId() {
        return id++;
    }
}
