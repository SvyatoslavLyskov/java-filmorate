package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NonNull

public class Film {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 1, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(1)
    private long duration;
}
