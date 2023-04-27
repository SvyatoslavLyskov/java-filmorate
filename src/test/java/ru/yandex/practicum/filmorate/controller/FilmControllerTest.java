package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    private final FilmController controller = new FilmController(new FilmService(new InMemoryFilmStorage(),
            new InMemoryUserStorage()));


    @Test
    public void shouldNotCreateFilmWithLongDescription() {
        Film film = new Film(1, "film", "description", LocalDate.of(1992, 10, 15),
                50, 0);
        film.setDescription("over200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover" +
                "over200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover200symbolsover200s" +
                "over200symbolsover200symbolsover200symbols");
        film.setId(film.getId());
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createFilm(film));
        Assertions.assertEquals("Описание не более 200 символов", exception.getMessage());
    }

    @Test
    public void shouldNotCreateFilmWithBadReleaseDate() {
        Film film = new Film(1, "film", "description", LocalDate.of(1852, 10, 15),
                50, 0);
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createFilm(film));
        Assertions.assertEquals("Ошибка, дата релиза — не раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    public void shouldNotCreateFilmWithBadDuration() {
        Film film = new Film(1, "film", "description", LocalDate.of(1995, 10, 15),
                -50L, 0);
        film.setId(film.getId());
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createFilm(film));
        Assertions.assertEquals("некорректная продолжительность фильма", exception.getMessage());
    }

    @Test
    public void shouldNotCreateFilmWithEmptyName() {
        Film film = new Film(1, "", "description", LocalDate.of(1994, 10, 15),
                50, 0);
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createFilm(film));
        Assertions.assertEquals("фильм без названия", exception.getMessage());
    }
}