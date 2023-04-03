package ru.yandex.practicum.filmorate.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LikeDaoTest {
    private final UserDao userDao;
    private final FilmDao filmDao;
    private final LikeDao likeDao;

    @Test
    public void testAddLike() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        Optional<User> optionalUser =  userDao.addUser(user);
        Optional<Film> optionalFilm = filmDao.addFilm(film);
        assertTrue(optionalFilm.isPresent());
        assertTrue(optionalUser.isPresent());
        long filmId = optionalFilm.get().getId();
        long userId = optionalUser.get().getId();
        likeDao.addLike(filmId, userId);
        assertEquals(List.of(optionalFilm.get()), likeDao.getUsersFavoriteFilms(userId));
        assertEquals(List.of(optionalUser.get()), likeDao.getFilmFollowers(filmId));
    }

    @Test
    public void testDeleteLike() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        Optional<User> optionalUser =  userDao.addUser(user);
        Optional<Film> optionalFilm = filmDao.addFilm(film);
        assertTrue(optionalFilm.isPresent());
        assertTrue(optionalUser.isPresent());
        long filmId = optionalFilm.get().getId();
        long userId = optionalUser.get().getId();
        likeDao.addLike(filmId, userId);
        assertEquals(List.of(optionalFilm.get()), likeDao.getUsersFavoriteFilms(userId));
        assertEquals(List.of(optionalUser.get()), likeDao.getFilmFollowers(filmId));
        likeDao.deleteLike(filmId, userId);
        assertEquals(Collections.EMPTY_LIST, likeDao.getUsersFavoriteFilms(userId));
        assertEquals(Collections.EMPTY_LIST, likeDao.getFilmFollowers(filmId));
    }

    @Test
    public void testGetTopFilm() {
        Film film1 = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        Film film2 = Film.builder()
                .name("Film2")
                .description("Description2")
                .releaseDate(LocalDate.of(1991, 10, 10))
                .duration(60)
                .build();
        User user1 = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User user2 = User.builder()
                .name("User2")
                .login("login2")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        Optional<User> optionalUser1 =  userDao.addUser(user1);
        Optional<User> optionalUser2 =  userDao.addUser(user2);
        Optional<Film> optionalFilm1 = filmDao.addFilm(film1);
        Optional<Film> optionalFilm2 = filmDao.addFilm(film2);
        assertTrue(optionalFilm1.isPresent() && optionalFilm2.isPresent());
        assertTrue(optionalUser1.isPresent() && optionalUser2.isPresent());
        long film1Id = optionalFilm1.get().getId();
        long film2Id = optionalFilm2.get().getId();
        long user1Id = optionalUser1.get().getId();
        long user2Id = optionalUser2.get().getId();
        assertEquals(2, likeDao.getTop(2).size());
        assertEquals(1, likeDao.getTop(1).size());
        likeDao.addLike(film1Id, user1Id);
        assertEquals(optionalFilm1.get(), likeDao.getTop(2).get(0));
        assertEquals(optionalFilm2.get(), likeDao.getTop(2).get(1));
        likeDao.addLike(film2Id, user1Id);
        likeDao.addLike(film2Id, user2Id);
        assertEquals(optionalFilm2.get(), likeDao.getTop(2).get(0));
        assertEquals(optionalFilm1.get(), likeDao.getTop(2).get(1));
    }
}