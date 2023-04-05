package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Component("FriendDao")
public class FriendDao implements FriendStorage {
    JdbcTemplate jdbcTemplate;
    UserDao userDao;

    @Autowired
    public FriendDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        if (userDao.containsUser(id) && userDao.containsUser(friendId)) {
            int statusY = getFriendStatus(id, friendId);
            int statusF = getFriendStatus(friendId, id);
            if (statusY == 0 && statusF == 0) {
                String sqlQuery = "insert into friends (user_id, friend_id, status) " +
                        "values (?, ?, ?)";
                jdbcTemplate.update(sqlQuery, id, friendId, 1);
            } else if (statusY == 0 && statusF == 1) {
                String sqlQuery = "insert into friends (user_id, friend_id, status) " +
                        "values (?, ?, ?)";
                jdbcTemplate.update(sqlQuery, id, friendId, 2);
                sqlQuery = "update friends " +
                        "set status = ? " +
                        "where user_id = ? and friend_id =?";
                jdbcTemplate.update(sqlQuery, 2, friendId, id);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        if (userDao.containsUser(id) && userDao.containsUser(friendId)) {
            int status = getFriendStatus(friendId, id);
            if (status == 2) {
                String sqlQuery = "update friends " +
                        "set status = ? " +
                        "where user_id = ? and friend_id =?";
                jdbcTemplate.update(sqlQuery, 1, friendId, id);
            }
            String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlQuery, id, friendId);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "select * from users " +
                "where user_id in " +
                "(select friend_id from friends where user_id = ?) order by user_id";
        return jdbcTemplate.query(sql, userDao::makeUser, id);
    }

    private int getFriendStatus(Long id, Long friendId) {
        String sql = "select status from friends where user_id=? and friend_id=?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id, friendId);
        if (rs.next()) {
            return rs.getInt("status");
        } else {
            return 0;
        }
    }
}
