package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @PositiveOrZero(message = "{validation.id.PositiveOrZero}")
    private final int id;
    @NotEmpty(message = "{validation.login.NotEmpty}")
    @Pattern(regexp = "/^([а-яё]+|[a-z]+)$/iu")
    private final String login;
    @NotEmpty(message = "{validation.email.NotEmpty}")
    @Email(message = "{validation.email.Type}")
    private String email;
    private String name;
    @NotNull(message = "{validation.birthday.NotNull}")
    @PastOrPresent(message = "{validation.birthday.PastOrPresent}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;
}
