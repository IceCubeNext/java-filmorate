package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final LikeService likeService;

    @Autowired
    public FilmController (FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    @GetMapping("{id}")
    public Optional<Film> getFilm(@PathVariable Long id) {
        log.debug(String.format("Get film with id=%d", id));
        return filmService.getFilm(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.debug(String.format("Get films. Return %d items", filmService.getFilms().size()));
        return filmService.getFilms();
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug(String.format("Get top %d films. Return %d items", count, likeService.getTop(count).size()));
        return likeService.getTop(count);
    }

    @PostMapping
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        log.debug(String.format("Post: add new film %s", film));
        return filmService.addFilm(film);
    }

    @PutMapping
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) {
        log.debug(String.format("Put: film update %s", film));
        return filmService.updateFilm(film);
    }

    @PutMapping("{id}/like/{userId}")
    public boolean setLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        log.debug(String.format("Put: user id=%d set like to film with id=%d", userId, id));
        return likeService.addLike(userId, id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public boolean deleteLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        log.debug(String.format("Delete: user id=%d delete like from film with id=%d", userId, id));
        return likeService.deleteLike(userId, id);
    }
}
