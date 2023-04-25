package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmServiceTest {
    private final FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

    @Test
    public void shouldCreateFilm() {
        Film film = new Film(1, "film", "description", LocalDate.of(2020, 1, 1),
                50, 0);
        assertEquals(film, filmService.createFilm(film));
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = new Film(1, "film", "description", LocalDate.of(2020, 1, 1),
                50, 0);
        filmService.createFilm(film);
        Film filmUpd = new Film(1, "filmupd", "descriptionupd",
                LocalDate.of(2020, 1, 1), 50, 0);
        filmService.updateFilm(filmUpd);
        assertEquals(filmUpd, filmService.updateFilm(filmUpd));
    }

    @Test
    public void shouldNotUpdateFilm() {
        Film film = new Film(1, "film", "description", LocalDate.of(2020, 1, 1),
                50, 0);
        assertThrows(ObjectNotFoundException.class, () -> filmService.updateFilm(film));
        assertTrue(filmService.getFilms().isEmpty());
    }
}