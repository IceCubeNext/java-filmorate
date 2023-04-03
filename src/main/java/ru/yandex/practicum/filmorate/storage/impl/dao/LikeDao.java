package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component("LikeDao")
public class LikeDao implements LikeStorage {
    JdbcTemplate jdbcTemplate;
    UserDao userDao;
    FilmDao filmDao;
    public LikeDao(JdbcTemplate jdbcTemplate, UserDao userDao, FilmDao filmDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.filmDao = filmDao;
    }

    @Override
    public boolean addLike(long id, long userId) {
        if (userDao.containsUser(userId) && filmDao.containsFilm(id)) {
            String sqlQuery = "merge into likes KEY (user_id, film_id) " +
                              "values (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLike(long id, long userId) {
        if (userDao.containsUser(userId) && filmDao.containsFilm(id)) {
            String sqlQuery = "delete from likes where user_id = ? and film_id = ?";
            jdbcTemplate.update(sqlQuery, userId, id);
            return true;
        }
        return false;
    }

    @Override
    public List<Film> getUsersFavoriteFilms(long id) {
        if (userDao.containsUser(id)) {
            String sql = "select * from films " +
                         "where film_id in " +
                         "(select film_id from likes where user_id = ?)";
            return jdbcTemplate.query(sql, filmDao::makeFilm, id);
        }
        return Collections.emptyList();
    }

    @Override
    public List<User> getFilmFollowers(long id) {
        if (filmDao.containsFilm(id)) {
            String sql = "select * from users " +
                         "where user_id in " +
                         "(select user_id from likes where film_id = ?)";
            return jdbcTemplate.query(sql, userDao::makeUser, id);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Film> getTop(int count) {
        String sql = "select f.film_id, " +
                            "f.title, " +
                            "f.description, " +
                            "f.release_date, " +
                            "f.duration, " +
                            "f.mpa_rating from films AS f " +
                    "left join likes As l on f.film_id = l.film_id " +
                    "group by f.film_id " +
                    "order by sum(l.film_id) desc, f.title " +
                    "limit(?)";
        return jdbcTemplate.query(sql, filmDao::makeFilm, count);
    }
}