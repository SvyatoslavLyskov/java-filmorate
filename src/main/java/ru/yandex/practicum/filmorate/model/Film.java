package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NonNull
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    long id;
    @NotBlank
    String name;
    @NotBlank
    @Size(min = 1, max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @Min(1)
    long duration;
    Set<Long> likesRate;
    int rate;

    public Film(long id, String name, String description, LocalDate releaseDate, long duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.likesRate = new HashSet<>();
    }

    public void addLike(Long id) {
        getLikesRate().add(id);
    }

    public void removeLike(Long id) {
        getLikesRate().remove(id);
    }
}
