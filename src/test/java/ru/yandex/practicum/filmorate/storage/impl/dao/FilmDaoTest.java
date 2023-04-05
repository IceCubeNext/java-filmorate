package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDaoTest {
    private final FilmDao filmDao;

    @Test
    public void testContainsFilm() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        assertTrue(filmDao.containsFilm(1L));
        assertThrows(NotFoundException.class, () -> filmDao.containsFilm(2L));
    }

    @Test
    public void testAddFilm() {
        assertThrows(NotFoundException.class, () -> filmDao.containsFilm(1L));
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        assertTrue(filmDao.containsFilm(1L));
    }

    @Test
    public void testFindFilmById() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        Optional<Film> filmOptional = filmDao.getFilmById(1L);
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
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film1);
        Film film2 = Film.builder()
                .name("Film2")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film2);
        List<Film> films = filmDao.getFilms();
        assertEquals(2, films.size());
        assertEquals(1, films.get(0).getId());
        assertEquals(2, films.get(1).getId());
    }

    @Test
    public void testGetEmptyFilms() {
        List<Film> films = filmDao.getFilms();
        assertEquals(0, films.size());
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
        Optional<Film> optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals("Film", optionalFilm.get().getName());

        film = optionalFilm.get();
        film.setName("NewFilm");
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals("NewFilm", optionalFilm.get().getName());
    }

    @Test
    public void testUpdateFilmDescription() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        Optional<Film> optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals("Description", optionalFilm.get().getDescription());

        film = optionalFilm.get();
        film.setDescription("NewDescription");
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals("NewDescription", optionalFilm.get().getDescription());
    }

    @Test
    public void testUpdateReleaseDate() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        Optional<Film> optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(LocalDate.of(2000, 10, 10), optionalFilm.get().getReleaseDate());

        film = optionalFilm.get();
        film.setReleaseDate(LocalDate.of(2000, 10, 1));
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(LocalDate.of(2000, 10, 1), optionalFilm.get().getReleaseDate());
    }

    @Test
    public void testUpdateDuration() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        Optional<Film> optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(60, optionalFilm.get().getDuration());

        film = optionalFilm.get();
        film.setDuration(30);
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(30, optionalFilm.get().getDuration());
    }

    @Test
    public void testUpdateMpa() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        Optional<Film> optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(new Mpa(1, "G"), optionalFilm.get().getMpa());

        film = optionalFilm.get();
        film.setMpa(new Mpa(2, "PG"));
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(new Mpa(2, "PG"), optionalFilm.get().getMpa());
    }

    @Test
    public void testUpdateGenres() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        Optional<Film> optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(List.of(new Genre(1, "Комедия")), optionalFilm.get().getGenres());

        film = optionalFilm.get();
        film.setGenres(null);
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(Collections.emptyList(), optionalFilm.get().getGenres());

        film.setGenres(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")));
        filmDao.updateFilm(film);
        optionalFilm = filmDao.getFilmById(1L);
        assertTrue(optionalFilm.isPresent());
        assertEquals(List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")), optionalFilm.get().getGenres());
    }

    @Test
    public void testDeleteFilm() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        filmDao.addFilm(film);
        assertTrue(filmDao.containsFilm(1L));
        filmDao.deleteFilm(1L);
        assertThrows(NotFoundException.class, () -> filmDao.containsFilm(1L));
    }
}