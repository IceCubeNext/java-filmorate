package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("FilmGenreDao")
public class FilmGenreDao {
    JdbcTemplate jdbcTemplate;
    GenreDao genreDao;

    public FilmGenreDao(JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    public List<Genre> getFilmGenres(Long id) {
        String sqlQuery = "select * from film_genre where film_id = ? order by genre_id";
        return jdbcTemplate.query(sqlQuery, this::makeFilmGenre, id).stream()
                .map(FilmGenre::getGenreId)
                .map(genreDao::getGenreById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void deleteFilmGenres(Long id) {
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public void addGenreToFilm(Long filmId, Integer genreId) {
        if (genreDao.containsGenre(genreId)) {
            String sqlQuery = "merge into film_genre KEY (film_id, genre_id) " +
                    "values(?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, genreId);
        }
    }

    private FilmGenre makeFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return new FilmGenre(rs.getLong("film_id"),
                rs.getInt("genre_id"));
    }
}