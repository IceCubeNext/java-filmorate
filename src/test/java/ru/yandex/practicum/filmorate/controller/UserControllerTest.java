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
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserController userController;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        //userController.clearData();
    }

    @Test
    public void getEmptyUsers() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    public void addNormalUser() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .id(1)
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        String json = mapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(
                        post("/users")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
                        .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        assertEquals(json, actualJson);
    }

    @Test
    public void addUserWithIncorrectEmail() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .id(1)
                .name("User")
                .login("login")
                .email("mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(
                        post("/users")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addUserWithSpaceInLogin() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .id(1)
                .name("User")
                .login("login login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(
                        post("/users")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addUserWithEmptyLogin() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .id(1)
                .name("User")
                .login("")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
                .build();
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(
                        post("/users")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addUserWithBirthdayInFuture() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .id(1)
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(
                        post("/users")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addUserWithBirthdayToday() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .id(1)
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.now())
                .build();
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(
                        post("/users")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addUserWithEmptyName() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .id(1)
                .name("")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.now())
                .build();
        String json = mapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(
                        post("/users")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        user = User.builder()
                .id(1)
                .name("login")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.now())
                .build();
        json = mapper.writeValueAsString(user);
        assertEquals(json, actualJson);
    }
}