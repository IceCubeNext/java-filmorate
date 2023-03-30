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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM user WHERE user_id=?", id);
        if(userRows.next()) {
            log.info("Found user with: id={}", id);
            return true;
        } else {
            log.info(String.format("User with id=%d not found", id));
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    @Override
    public Optional<User> addUser(User user) {
        try {
            String sqlQuery = "INSERT INTO users (login, email, name, birthday) " +
                              "VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getName());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                return stmt;
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            user.setId(id);
            log.debug("Successfully added user with id={}", id);
        } catch (DataAccessException e) {
            log.error("Error occurred while adding user: ", e);
        }
        return Optional.of(user);
    }

    public Optional<User> getUserById(long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id=?";
        User user = jdbcTemplate.queryForObject(sqlQuery, this::makeUser, id);
        if(user != null) {
            log.info("Found user with: id={}", id);
            return Optional.of(user);
        } else {
            log.info("User with id={} not found", id);
            throw new NotFoundException(String.format("User with id=%d not found", id));
        }
    }

    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    public Optional<User> updateUser(User user) {
        try {
            String sqlQuery = "UPDATE users " +
                              "SET login = ?, email = ?, name = ?, birthday = ? " +
                              "WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery,
                                user.getLogin(),
                                user.getEmail(),
                                user.getName(),
                                user.getBirthday(),
                                user.getId());
            log.debug("Successfully updated user with id={}", user.getId());
        } catch (DataAccessException e) {
            log.error("Error occurred while updating user: ", e);
        }
        return Optional.of(user);
    }

    public Optional<User> deleteUser(long id) {
        try {
            Optional<User> user = getUserById(id);
            String sqlQuery = "DELETE FROM users WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery, id);
            log.debug("Successfully deleted user with id={}", id);
            return user;
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting user: ", e);
        }
        return Optional.empty();
    }

    public boolean addFriend(long id, long friendId) {
        try {
            String sqlQuery = "INSERT INTO friend (user_id, friend_id, status) " +
                              "VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQuery, id, friendId, 1);
            log.debug("User id={} add to friend user id={}", id, friendId);
            return true;
        } catch (DataAccessException e) {
            log.error("Error occurred while adding to friend: ", e);
        }
        return false;
    }

    public boolean deleteFriend(long id, long friendId) {
        try {
            String sqlQuery = "DELETE FROM users WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sqlQuery, id, friendId);
            log.debug("User id={} delete from friend user id={}", id, friendId);
            return true;
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting from friend: ", e);
        }
        return false;
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
