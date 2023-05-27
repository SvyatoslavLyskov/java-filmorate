package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    FilmStorage filmStorage;
    private static long id = 1;
    private static final LocalDate EPOCH_FILMS = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public Collection<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        if (isValid(film)) {
            film.setId(id);
            films.put(id, film);
            id++;
            log.info("Фильм создан {}", film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Нет такого фильма");
            throw new ObjectNotFoundException("Нет такого фильма");
        } else if (isValidDate(film)) {
            films.put(film.getId(), film);
            log.info("Фильм обновлен {}", film);
        }
        return film;
    }

    private boolean isValidDate(Film film) {
        return film.getReleaseDate().isAfter(EPOCH_FILMS);
    }

    private boolean isValid(Film film) {
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

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id) || id == null) {
            throw new ObjectNotFoundException("фильм с id" + id + " не найден.");
        }
        log.info("фильм c id {} успешно получен.", id);
        return films.get(id);
    }

    @Override
    public boolean isValid(Long id) {
        return filmStorage.isValid(id);
    }
}
