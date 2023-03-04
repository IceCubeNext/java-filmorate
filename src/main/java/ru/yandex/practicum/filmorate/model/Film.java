package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.DateAfter;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @PositiveOrZero(message = "id should be positive or zero")
    private int id;
    @NotEmpty(message = "name should not be empty")
    private String name;
    @Size(max = 200, message = "description size should not be more than 200")
    private String description;
    @NotNull(message = "releaseDate should not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateAfter(date = "1895-12-28")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NotNull(message = "duration should not be null")
    @PositiveOrZero(message = "duration should be positive or zero")
    private int duration;

}