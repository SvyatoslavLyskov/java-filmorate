package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int id = 1;
    private static final LocalDate EPOCH_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);

    @GetMapping
    public Collection<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (isValid(film)) {
            film.setId(id);
            films.put(id, film);
            id++;
            log.info("Фильм создан {}", film);
        }
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Нет такого фильма");
            throw new ValidationException("Нет такого фильма");
        } else if (isValidDate(film)) {
            films.put(film.getId(), film);
            log.info("Фильм обновлен {}", film);
        }
        return film;
    }

    private boolean isValidDate(@Valid Film film) {
        return film.getReleaseDate().isAfter(EPOCH_FILMS);
    }

    private boolean isValid(@Valid Film film) {
        if (films.containsKey(film.getId())) {
            log.warn("Такой фильм уже есть.");
            throw new ValidationException("Такой фильм уже есть.");
        } else if (film.getReleaseDate().isBefore(EPOCH_FILMS)) {
            log.warn("Неверная дата релиза");
            throw new ValidationException("Ошибка, дата релиза — не раньше 28 декабря 1895 года.");
        } else if (film.getDescription().length() > 200) {
            log.warn("Описание не более 200 символов");
            throw new ValidationException("Описание не более 200 символов");
        } else if (!(film.getDuration() > 0)) {
            log.warn("некорректная продолжительность фильма");
            throw new ValidationException("некорректная продолжительность фильма");
        } else if (isBlank(film.getName())) {
            log.warn("фильм без названия");
            throw new ValidationException("фильм без названия");
        } else {
            return true;
        }
    }
}