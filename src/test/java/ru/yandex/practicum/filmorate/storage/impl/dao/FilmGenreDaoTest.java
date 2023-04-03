package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmGenreDaoTest {
    private final FilmGenreDao filmGenreDao;
    private final FilmDao filmDao;

    @Test
    public void testGetFilmGenres() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")))
                .build();
        Optional<Film> optionalFilm = filmDao.addFilm(film);
        assertTrue(optionalFilm.isPresent());
        Long filmId = optionalFilm.get().getId();
        List<Genre> filmGenres = filmGenreDao.getFilmGenres(filmId);
        assertEquals(2, filmGenres.size());
        assertEquals(filmGenres.get(0), new Genre(1, "Комедия"));
        assertEquals(filmGenres.get(1), new Genre(2, "Драма"));
    }

    @Test
    public void testDeleteFilmGenres() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")))
                .build();
        Optional<Film> optionalFilm = filmDao.addFilm(film);
        assertTrue(optionalFilm.isPresent());
        Long filmId = optionalFilm.get().getId();
        assertEquals(2, filmGenreDao.getFilmGenres(filmId).size());
        filmGenreDao.deleteFilmGenres(filmId);
        assertEquals(0, filmGenreDao.getFilmGenres(filmId).size());
    }

    @Test
    public void testAddFilmGenre() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        Optional<Film> optionalFilm = filmDao.addFilm(film);
        assertTrue(optionalFilm.isPresent());
        Long filmId = optionalFilm.get().getId();
        assertEquals(1, filmGenreDao.getFilmGenres(filmId).size());
        filmGenreDao.addGenreToFilm(filmId, 2);
        List<Genre> filmGenres = filmGenreDao.getFilmGenres(filmId);
        assertEquals(2, filmGenres.size());
        assertEquals(filmGenres.get(0), new Genre(1, "Комедия"));
        assertEquals(filmGenres.get(1), new Genre(2, "Драма"));
    }
}