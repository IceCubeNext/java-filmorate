package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @PositiveOrZero(message = "{validation.id.PositiveOrZero}")
    private final int id;
    @NotEmpty(message = "{validation.name.NotEmpty}")
    private final String name;
    @Size(max = 200, message = "{validation.description.Size}")
    private final String description;
    @NotNull(message = "{validation.releaseDate.NotNull}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private final LocalDate releaseDate;
    private final Duration duration;
}