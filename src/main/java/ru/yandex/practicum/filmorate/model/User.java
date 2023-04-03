package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @PositiveOrZero(message = "Id should be positive or zero")
    private Long id;
    @NotEmpty(message = "Login should not be empty")
    @Pattern(regexp = "([а-яёА-ЯЁ0-9]+|[a-zA-Z0-9]+)")
    private String login;
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email incorrect")
    private String email;
    private String name;
    @NotNull(message = "Birthday should not be empty")
    @PastOrPresent(message = "Birthday should not be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}

