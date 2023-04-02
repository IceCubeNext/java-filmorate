package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("UserDao")
public class UserDao implements UserStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean containsUser(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id=?", id);
        if(userRows.next()) {
            return true;
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    @Override
    public Optional<User> addUser(User user) throws DataAccessException{
        String sqlQuery = "insert into users (login, email, name, birthday) " +
                          "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            long id = keyHolder.getKey().longValue();
            user.setId(id);
            return Optional.of(user);
        } else {
            throw new RuntimeException(String.format("Error occurred while updating user %s", user));
        }
    }

    @Override
    public Optional<User> getUserById(long id) {
        try {
            String sql = "select * from users where user_id=?";
            User user= jdbcTemplate.queryForObject(sql, this::makeUser, id);
            if (user != null) {
                return Optional.of(user);
            } else {
                throw new NotFoundException(String.format("User with id=%d not found", id));
            }
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery = "update users " +
                          "set login = ?, email = ?, name = ?, birthday = ? " +
                          "where user_id = ?";
        int rows = jdbcTemplate.update(sqlQuery,
                            user.getLogin(),
                            user.getEmail(),
                            user.getName(),
                            user.getBirthday(),
                            user.getId());
        if (rows > 0) {
            return Optional.of(user);
        } else {
            throw new NotFoundException(String.format("User with id=%d not found", user.getId()));
        }
    }

    @Override
    public Optional<User> deleteUser(long id) {
        Optional<User> user = getUserById(id);
        String sqlQuery = "delete from users where user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return user;
    }

    public User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
