package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateMinValidator implements ConstraintValidator<DateMin, LocalDate> {

    private LocalDate minDate;
    public void initialize(DateMin constraint) {
        minDate = LocalDate.parse(constraint.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date != null && (date.isAfter(minDate) || date.isEqual(minDate));
    }
}
