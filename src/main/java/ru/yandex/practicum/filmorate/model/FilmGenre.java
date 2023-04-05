package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilmGenre {
    private Long filmId;
    private Integer genreId;

    public FilmGenre(Long filmId, Integer genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }
}
