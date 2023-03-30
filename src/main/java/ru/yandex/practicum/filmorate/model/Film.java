package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validation.DateMin;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    @PositiveOrZero(message = "Id should be positive or zero")
    private long id;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @Size(max = 200, message = "Description size should not be more than 200")
    private String description;
    @NotNull(message = "ReleaseDate should not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateMin(date = "1895-12-28")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NotNull(message = "Duration should not be null")
    @PositiveOrZero(message = "Duration should be positive or zero")
    private int duration;
    private String mpaRating;
    @Builder.Default
    private Set<Long> likes = new HashSet<>();
}