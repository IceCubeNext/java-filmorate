package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Set<Film> filmsLibrary = new HashSet<>();
    @GetMapping
    public List<Film> getFilms(){
        return new ArrayList<>(filmsLibrary);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        filmsLibrary.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmsLibrary.remove(film);
        filmsLibrary.add(film);
        return film;
    }
}
