package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    FilmController controller = new FilmController();

    @Test
    public void shouldNotCreateFilmWithLongDescription() {
        Film film = new Film();
        film.setName("Matrix3");
        film.setDuration(115);
        film.setDescription("over200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover" +
                "over200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover200s" +
                "over200symbolsover200symbolsover200symbols");
        film.setReleaseDate(LocalDate.of(2022, 12, 1));
        film.setId(film.getId());
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    public void shouldNotCreateFilmWithBadReleaseDate() {
        Film film = new Film();
        film.setName("matrix2");
        film.setDuration(100);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1852, 10, 15));

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    public void shouldNotCreateFilmWithBadDuration() {
        Film film = new Film();
        film.setName("Matrix");
        film.setDuration(-100L);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1992, 7, 30));
        film.setId(film.getId());
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    public void shouldNotCreateFilmWithEmptyName() {
        Film film = new Film();
        film.setName("");
        film.setDuration(100);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1994, 11, 29));

        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }
}