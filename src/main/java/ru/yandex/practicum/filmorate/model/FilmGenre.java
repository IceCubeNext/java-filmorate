package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilmGenre {
    private Long filmId;
    private Integer genre_id;

    public FilmGenre(Long filmId, Integer genreId) {
        this.filmId = filmId;
        this.genre_id = genreId;
    }
}
