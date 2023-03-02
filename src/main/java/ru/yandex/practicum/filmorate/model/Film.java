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
    @PositiveOrZero(message = "id should be positive or zero")
    private final int id;
    @NotEmpty(message = "name should not be empty")
    private final String name;
    @Size(max = 200, message = "description size should not be more than 200")
    private final String description;
    @NotNull(message = "releaseDate should not be null")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private final LocalDate releaseDate;
    @Positive(message = "duration should be positive")
    private final Duration duration;
}