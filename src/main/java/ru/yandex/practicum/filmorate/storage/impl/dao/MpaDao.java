package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("MpaDao")
public class MpaDao {
    JdbcTemplate jdbcTemplate;

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Mpa> getMpaById(Integer id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from mpa_rating where mpa_id=?", id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(mpaRows.getInt("mpa_id"), mpaRows.getString("name"));
            return Optional.of(mpa);
        } else {
            throw new NotFoundException(String.format("Mpa_rating with id=%d not found", id));
        }
    }

    public List<Mpa> getMpaTypes() {
        String sqlQuery = "select * from mpa_rating order by mpa_id";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"),
                rs.getString("name"));
    }
}
