package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDaoTest {
    private final MpaDao mpaDao;

    @Test
    public void testFindMpaById() {
        Optional<Mpa> mpaOptional = mpaDao.getMpaById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    public void testGetMpa() {
        List<Mpa> mpaList = mpaDao.getMpaTypes();
        assertEquals(5, mpaList.size());
        assertEquals(mpaList.get(0), new Mpa(1, "G"));
        assertEquals(mpaList.get(1), new Mpa(2, "PG"));
        assertEquals(mpaList.get(2), new Mpa(3, "PG-13"));
        assertEquals(mpaList.get(3), new Mpa(4, "R"));
        assertEquals(mpaList.get(4), new Mpa(5, "NC-17"));
    }
}