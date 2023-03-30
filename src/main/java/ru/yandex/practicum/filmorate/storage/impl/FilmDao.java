package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("FilmDao")
public class FilmDao implements FilmStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean containsFilm(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id=?", id);
        if(filmRows.next()) {
            log.info("Found film with: id={}", id);
            return true;
        } else {
            log.info(String.format("Film with id=%d not found", id));
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        try {
            String sqlQuery = "INSERT INTO films (title, description, release_date, duration, mpa_rating) " +
                              "VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                stmt.setString(4, film.getMpaRating());
                return stmt;
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            film.setId(id);
            log.debug("Successfully added film with id={}", id);
        } catch (DataAccessException e) {
            log.error("Error occurred while adding film: ", e);
        }
        return Optional.of(film);
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        String sqlQuery = "SELECT * FROM films WHERE film_id=?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, id);
        if (film != null) {
            log.info("Found film with: id={}", id);
            return Optional.of(film);
        } else {
            log.info(String.format("Film with id=%d not found", id));
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        try {
            String sqlQuery = "UPDATE films " +
                              "SET title = ?, description = ?, release_date = ?, duration = ?, mpa_rating = ? " +
                              "WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                                film.getName(),
                                film.getDescription(),
                                film.getReleaseDate(),
                                film.getDuration(),
                                film.getMpaRating(),
                                film.getId());
            log.debug("Successfully updated film with id={}", film.getId());
        } catch (DataAccessException e) {
            log.error("Error occurred while updating film: ", e);
        }
        return Optional.of(film);
    }

    @Override
    public Optional<Film> deleteFilm(long id) {
        try {
            Optional<Film> film = getFilmById(id);
            String sqlQuery = "DELETE FROM films WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, id);
            log.debug("Successfully deleted film with id={}", id);
            return film;
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting film: ", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean addLike(long id, long userId) {
        try {
            String sqlQuery = "INSERT INTO likes (user_id, film_id) " +
                              "VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, id);
            log.debug("User id={} set like to film id={}", userId, id);
            return true;
        } catch (DataAccessException e) {
            log.error("Error occurred while setting like to film ", e);
        }
        return false;
    }

    @Override
    public boolean deleteLike(long id, long userId) {
        try {
            String sqlQuery = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
            jdbcTemplate.update(sqlQuery, userId, id);
            log.debug("User id={} delete like from film id={}", userId, id);
            return true;
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting like from film ", e);
        }
        return false;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("title"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpaRating(rs.getString("mpa_rating"))
                .build();
    }
}
