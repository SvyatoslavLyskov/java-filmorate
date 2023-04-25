package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final Comparator<Film> COMPARATOR_BY_LIKES = (f1, f2) -> f2.getLikesRate().size()
            - f1.getLikesRate().size();

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.addLike(userId);
        log.info("Пользователь '{}' оценил фильм '{}'", user.getName(), film.getName());
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.removeLike(user.getId());
        log.info("Пользователь '{}' убрал оценку с фильма '{}'", user.getName(), film.getName());
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> result = new ArrayList<>(filmStorage.getFilms());
        return result
                .stream()
                .sorted(COMPARATOR_BY_LIKES)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }
}