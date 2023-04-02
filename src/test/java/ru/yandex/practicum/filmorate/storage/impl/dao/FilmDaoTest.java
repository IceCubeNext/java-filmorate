package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoTest {
    private final FilmDao filmDao;

    @Test
    public void testContainsFilm() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        filmDao.addFilm(film);
        assertTrue(filmDao.containsFilm(1));
        assertThrows(NotFoundException.class, () -> filmDao.containsFilm(2));
    }

    @Test
    public void testAddFilm() {
        assertThrows(NotFoundException.class, () -> filmDao.containsFilm(1));
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        filmDao.addFilm(film);
        assertTrue(filmDao.containsFilm(1));
    }

    @Test
    public void testFindFilmById() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        filmDao.addFilm(film);
        Optional<Film> filmOptional = filmDao.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(flm ->
                        assertThat(flm).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetFilms() {
        Film film1 = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        filmDao.addFilm(film1);
        Film film2 = Film.builder()
                .name("Film2")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        filmDao.addFilm(film2);
        List<Film> films = filmDao.getFilms();
        assertEquals(2, films.size());
        assertEquals(1, films.get(0).getId());
        assertEquals(2, films.get(1).getId());
    }

    @Test
    public void testUpdateFilmName() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        Optional<Film> optionalFilm = filmDao.getFilmById(1);
        assertTrue(optionalFilm.isPresent());
        assertEquals("Film", optionalFilm.get().getName());

        film.setName("NewFilm");
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1);
        assertTrue(optionalFilm.isPresent());
        assertEquals("NewFilm", optionalFilm.get().getName());
    }

    @Test
    public void testGetEmptyFilms() {
        List<Film> films = filmDao.getFilms();
        assertEquals(0, films.size());
    }
}