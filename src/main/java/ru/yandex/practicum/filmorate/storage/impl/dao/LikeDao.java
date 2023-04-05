package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Collections;
import java.util.List;

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
    public boolean addLike(Long id, Long userId) {
        if (userDao.containsUser(userId) && filmDao.containsFilm(id)) {
            String sqlQuery = "merge into likes KEY (user_id, film_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        if (userDao.containsUser(userId) && filmDao.containsFilm(id)) {
            String sqlQuery = "delete from likes where user_id = ? and film_id = ?";
            jdbcTemplate.update(sqlQuery, userId, id);
            return true;
        }
        return false;
    }

    @Override
    public List<Film> getUsersFavoriteFilms(Long id) {
        if (userDao.containsUser(id)) {
            String sql = "select f.film_id, " +
                    "f.title, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_rating, " +
                    "m.name as mpa_name " +
                    "from films as f " +
                    "left join mpa_rating as m on f.mpa_rating = m.mpa_id " +
                    "where film_id in " +
                    "(select film_id from likes where user_id = ?)";
            return jdbcTemplate.query(sql, filmDao::makeFilm, id);
        }
        return Collections.emptyList();
    }

    @Override
    public List<User> getFilmFollowers(Long id) {
        if (filmDao.containsFilm(id)) {
            String sql = "select * from users " +
                    "where user_id in " +
                    "(select user_id from likes where film_id = ?)";
            return jdbcTemplate.query(sql, userDao::makeUser, id);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Film> getTop(Integer count) {
        String sql = "select f.film_id, " +
                "f.title, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_rating, " +
                "m.name as mpa_name " +
                "from films AS f " +
                "left join mpa_rating as m on f.mpa_rating = m.mpa_id " +
                "left join likes As l on f.film_id = l.film_id " +
                "group by f.film_id " +
                "order by sum(l.film_id) desc, f.title " +
                "limit(?)";
        return jdbcTemplate.query(sql, filmDao::makeFilm, count);
    }
}
