package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoTest {
    private final GenreDao genreDao;

    @Test
    public void testContainsGenre() {
        assertTrue(genreDao.containsGenre(1));
        assertThrows(NotFoundException.class, () -> genreDao.containsGenre(7));
    }

    @Test
    public void testFindGenreById() {
        Optional<Genre> genreOptional = genreDao.getGenreById(1);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    public void testGetGenres() {
        List<Genre> genres = genreDao.getGenres();
        assertEquals(6, genres.size());
        assertEquals(genres.get(0), new Genre(1, "Комедия"));
        assertEquals(genres.get(1), new Genre(2, "Драма"));
        assertEquals(genres.get(2), new Genre(3, "Мультфильм"));
        assertEquals(genres.get(3), new Genre(4, "Триллер"));
        assertEquals(genres.get(4), new Genre(5, "Документальный"));
        assertEquals(genres.get(5), new Genre(6, "Боевик"));
    }
}