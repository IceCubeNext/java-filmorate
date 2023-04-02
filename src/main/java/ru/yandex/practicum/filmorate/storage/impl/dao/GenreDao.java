package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("GenreDao")
public class GenreDao {
    JdbcTemplate jdbcTemplate;
    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean containsGenre(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where genre_id=?", id);
        if(genreRows.next()) {
            return true;
        } else {
            throw new NotFoundException(String.format("Genre with id=%d not found", id));
        }
    }

    public Optional<Genre> getGenreById(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where genre_id=?", id);
        if(genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("genre_id"), genreRows.getString("name"));
            return Optional.of(genre);
        } else {
            throw new NotFoundException(String.format("Genre with id=%d not found", id));
        }
    }

    public List<Genre> getGenres() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"),
                         rs.getString("name"));
    }
}