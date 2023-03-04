package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> filmsLibrary = new HashMap<>();
    private int id = 0;
    public void clearData() {
        filmsLibrary.clear();
        id = 0;
    }
    @GetMapping
    public List<Film> getFilms(){
        return new ArrayList<>(filmsLibrary.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        int id = getNewId();
        film.setId(id);
        log.debug("create new film " + film);
        filmsLibrary.put(id, film);
        return ResponseEntity.ok(film).getBody();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (filmsLibrary.containsKey(film.getId())) {
            log.debug("film " + filmsLibrary.get(film.getId()) + " change data to " + film);
            filmsLibrary.put(film.getId(), film);
        } else {
            throw new NotFoundException("error while updating: film with id=" + film.getId() + " not found");
        }
        return ResponseEntity.ok(film).getBody();
    }

    private int getNewId() {
        return ++id;
    }
}
