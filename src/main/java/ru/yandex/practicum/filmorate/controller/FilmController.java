package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    public FilmController (FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms(){
        log.debug(String.format("Get films. Return %d items", filmService.getFilms().size()));
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug(String.format("Post new film %s", film));
        filmService.addFilm(film);
        return ResponseEntity.ok(film).getBody();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug(String.format("Put film %s", film));
        filmService.updateFilm(film);
        return ResponseEntity.ok(film).getBody();
    }
}
