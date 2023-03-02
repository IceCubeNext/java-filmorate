package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @PositiveOrZero(message = "id should be positive or zero")
    private final int id;
    @NotEmpty(message = "login should not be empty")
    @Pattern(regexp = "/^([а-яё]+|[a-z]+)$/iu")
    private final String login;
    @NotEmpty(message = "email should not be empty")
    @Email(message = "email incorrect")
    private String email;
    private String name;
    @NotNull(message = "birthday should not be empty")
    @PastOrPresent(message = "birthday should not be in future")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;
}
