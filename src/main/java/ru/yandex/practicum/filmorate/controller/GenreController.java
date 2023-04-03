package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("{id}")
    public Optional<Genre> getGenre(@PathVariable Integer id) {
        log.debug(String.format("Get: get genre with id=%d", id));
        return genreService.getGenre(id);
    }

    @GetMapping
    public List<Genre> getGenres() {
        log.debug(String.format("Get: get genres. Return %d items", genreService.getGenres().size()));
        return genreService.getGenres();
    }
}
