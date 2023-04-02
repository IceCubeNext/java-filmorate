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
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserController userController;
    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getEmptyUsers() throws Exception {
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addNormalUser() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
                .name("User")
                .login("login")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1991, 12, 12))
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
    public void addUserWithIncorrectEmail() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        User user = User.builder()
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
                .name("")
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
}