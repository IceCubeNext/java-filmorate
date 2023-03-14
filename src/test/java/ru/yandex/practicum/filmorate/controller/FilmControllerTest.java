package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    FilmController filmController;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        //filmController.clearData();
    }

    @Test
    public void getEmptyFilms() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void addNormalFilm() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
                .build();
        String json = mapper.writeValueAsString(film);
        MvcResult result = mockMvc.perform(
                        post("/films")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        assertEquals(json, actualJson);
    }

    @Test
    public void addFilmWithEmptyName() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
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

    @Test
    public void addFilmWithNegativeDuration() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(-60)
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
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(0)
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
                .id(1)
                .name("Film")
                .description("")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
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
                .id(1)
                .name("Film")
                .description(newString)
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(60)
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
                .id(1)
                .name("Film")
                .description(newString)
                .releaseDate(LocalDate.of(2010, 10, 10))
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

    @Test
    public void addFilmWithEarliestRelease() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Film film = Film.builder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(60)
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
                .id(1)
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