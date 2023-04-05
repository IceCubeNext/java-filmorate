package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    FilmController filmController;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getEmptyFilms() throws Exception {
        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addNormalFilm() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addFilmWithEmptyName() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1L)
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addFilmWithNegativeDuration() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(-60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addFilmWithZeroDuration() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(0)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addFilmWithEmptyDescription() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addFilmWith200SymbolsDescription() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        String newString = "a".repeat(200);
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description(newString)
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addFilmWithTooLongDescription() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        String newString = "a".repeat(201);
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description(newString)
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addFilmWithEarliestRelease() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(60)
                .mpa(new Mpa(1, "G"))
                .genres(List.of(new Genre(1, "Комедия")))
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addFilmWithTooEarlieRelease() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(60)
                .build();
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}