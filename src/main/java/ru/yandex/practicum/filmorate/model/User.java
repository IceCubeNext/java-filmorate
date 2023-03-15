package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    @PositiveOrZero(message = "id should be positive or zero")
    private long id;
    @NotEmpty(message = "login should not be empty")
    @Pattern(regexp =  "([а-яёА-ЯЁ]+|[a-zA-Z]+)")
    private String login;
    @NotEmpty(message = "email should not be empty")
    @Email(message = "email incorrect")
    private String email;
    private String name;
    @NotNull(message = "birthday should not be empty")
    @PastOrPresent(message = "birthday should not be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @Builder.Default
    Set<Long> friendsId = new HashSet<>();

}

