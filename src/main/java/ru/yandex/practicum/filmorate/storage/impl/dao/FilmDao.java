package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("FilmDao")
public class FilmDao implements FilmStorage {
    JdbcTemplate jdbcTemplate;
    MpaDao mpaDao;
    FilmGenreDao filmGenreDao;

    @Autowired
    public FilmDao(JdbcTemplate jdbcTemplate, MpaDao mpaDao, FilmGenreDao filmGenreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.filmGenreDao = filmGenreDao;
    }

    @Override
    public boolean containsFilm(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where film_id=?", id);
        if(filmRows.next()) {
            return true;
        } else {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        String sqlQuery = "insert into films (title, description, release_date, duration, mpa_rating) " +
                          "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            if (film.getMpa() != null) {
                stmt.setInt(5, film.getMpa().getId());
            } else {
                stmt.setInt(5, 0);
            }
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            long id = keyHolder.getKey().longValue();
            if (film.getGenres() != null) {
                for (Genre genre: film.getGenres()) {
                    filmGenreDao.addGenreToFilm(id, genre.getId());
                }
            }
            return getFilmById(id);
        } else {
            throw new RuntimeException(String.format("Error occurred while add film %s", film));
        }
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        try {
            String sql = "select * from films where film_id=?";
            Film film = jdbcTemplate.queryForObject(sql, this::makeFilm, id);
            if (film != null) {
                return Optional.of(film);
            } else {
                throw new NotFoundException(String.format("Film with id=%d not found", id));
            }
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "select * from films order by film_id";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQuery = "update films " +
                          "set title = ?, description = ?, release_date = ?, duration = ?, mpa_rating = ? " +
                          "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                            film.getName(),
                            film.getDescription(),
                            film.getReleaseDate(),
                            film.getDuration(),
                            film.getMpa().getId(),
                            film.getId());
        filmGenreDao.deleteFilmGenres(film.getId());
        if (film.getGenres() != null) {
            for (Genre genre: film.getGenres()) {
                filmGenreDao.addGenreToFilm(film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Optional<Film> deleteFilm(long id) {
        Optional<Film> film = getFilmById(id);
        String sqlQuery = "delete from films where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return film;
    }

    public Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                    .id(rs.getLong("film_id"))
                    .name(rs.getString("title"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .genres(filmGenreDao.getFilmGenres(rs.getLong("film_id")))
                    .build();
        Optional<Mpa> mpa = mpaDao.getMpaById(rs.getInt("mpa_rating"));
        mpa.ifPresent(film::setMpa);
        return film;
    }
}
