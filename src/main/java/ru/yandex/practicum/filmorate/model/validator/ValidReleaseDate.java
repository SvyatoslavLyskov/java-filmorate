package ru.yandex.practicum.filmorate.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmReleaseDateValidator.class)
public @interface ValidReleaseDate {

    String message() default "Дата выпуска фильма должна быть с 28 декабря 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}