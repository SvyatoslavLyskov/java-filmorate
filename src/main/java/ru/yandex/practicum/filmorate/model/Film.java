package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.validator.ValidReleaseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@NonNull
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    long id;
    @NotBlank
    String name;
    @NotBlank
    @Size(min = 1, max = 200)
    String description;
    @ValidReleaseDate
    LocalDate releaseDate;
    @Min(1)
    long duration;
    private LinkedHashSet<Genre> genres;
    private Mpa mpa;
}
