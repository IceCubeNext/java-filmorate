package ru.yandex.practicum.filmorate.storage.impl.memory;

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
    private Long id = 1L;

    public boolean containsFilm(Long id) {
        return films.containsKey(id);
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        long id = getNewId();
        film.setId(id);
        films.put(id, film);
        return Optional.of(films.get(id));
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        if(films.containsKey(id)) {
            return Optional.of(films.get(id));
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Optional<Film> updateFilm(Film film) {
        long id = film.getId();
        if(films.containsKey(id)) {
            films.put(id, film);
            return Optional.of(films.get(id));
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public Optional<Film> deleteFilm(Long id) {
        if(films.containsKey(id)) {
            Film film = films.get(id);
            films.remove(id);
            return Optional.of(film);
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    private Long getNewId() {
        return id++;
    }
}
