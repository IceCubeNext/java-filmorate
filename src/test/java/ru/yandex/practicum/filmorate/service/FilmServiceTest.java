package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmServiceTest {
    @Autowired
    FilmService filmService;
    @Autowired
    UserService userService;

    @Test
    public void addLikeNormal() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .build();
        Film oneFilm = filmService.addFilm(film);
        User user = User.builder()
                .name("Alex")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User oneUser = userService.addUser(user);
        assertEquals(0, filmService.getFilm(oneFilm.getId()).getLikes().size());
        filmService.addLike(oneUser.getId(), oneFilm.getId());
        assertEquals(1, filmService.getFilm(oneFilm.getId()).getLikes().size());
        assertTrue(filmService.getFilm(oneFilm.getId()).getLikes().contains(oneUser.getId()));
    }

    @Test
    public void addLikeFromNonexistentUser() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .build();
        Film oneFilm = filmService.addFilm(film);
        Exception exception = assertThrows(NotFoundException.class, () -> filmService.addLike(10, oneFilm.getId()));
        String expectedMessage = "User with id=10 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void addLikeToNonexistentFilm() {
        User user = User.builder()
                .name("Alex")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User oneUser = userService.addUser(user);
        Exception exception = assertThrows(NotFoundException.class, () -> filmService.addLike(oneUser.getId(), 10));
        String expectedMessage = "Film with id=10 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void deleteLikeNormal() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .build();
        Film oneFilm = filmService.addFilm(film);
        User user = User.builder()
                .name("Alex")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User oneUser = userService.addUser(user);
        filmService.addLike(oneUser.getId(), oneFilm.getId());
        assertEquals(1, filmService.getFilm(oneFilm.getId()).getLikes().size());
        assertTrue(filmService.getFilm(oneFilm.getId()).getLikes().contains(oneUser.getId()));
        filmService.deleteLike(oneUser.getId(), oneFilm.getId());
        assertEquals(0, filmService.getFilm(oneFilm.getId()).getLikes().size());
    }

    @Test
    public void deleteLikeFromNonexistentUser() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .build();
        Film oneFilm = filmService.addFilm(film);
        Exception exception = assertThrows(NotFoundException.class, () -> filmService.deleteLike(10, oneFilm.getId()));
        String expectedMessage = "User with id=10 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void deleteLikeToNonexistentFilm() {
        User user = User.builder()
                .name("Alex")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User oneUser = userService.addUser(user);
        Exception exception = assertThrows(NotFoundException.class, () -> filmService.deleteLike(oneUser.getId(), 10));
        String expectedMessage = "Film with id=10 not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void getTopDefineCount() {
        Film film1 = Film.builder()
                .name("Film 1")
                .description("Description 1")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(60)
                .build();
        Film film2 = Film.builder()
                .name("Film 2")
                .description("Description 2")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(100)
                .build();
        User user = User.builder()
                .name("Alex")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        User oneUser = userService.addUser(user);
        user = User.builder()
                .name("Oleg")
                .login("log")
                .email("oleg@mail.ru")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();
        User twoUser = userService.addUser(user);
        Film oneFilm = filmService.addFilm(film1);
        Film twoFilm = filmService.addFilm(film2);
        filmService.addLike(oneUser.getId(), oneFilm.getId());
        assertEquals(List.of(oneFilm), filmService.getTop(1));

        filmService.addLike(oneUser.getId(), twoFilm.getId());
        filmService.addLike(twoUser.getId(), twoFilm.getId());
        assertEquals(List.of(twoFilm), filmService.getTop(1));

        assertEquals(List.of(twoFilm, oneFilm), filmService.getTop(2));
        assertEquals(List.of(twoFilm, oneFilm), filmService.getTop(10));
    }
}