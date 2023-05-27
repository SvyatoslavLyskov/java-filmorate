package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likesDbStorage;


    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.isValid(filmId) || !userStorage.isExist(userId)) {
            log.warn("Не удалось добавить лайк, пользователь с ID {} или фильм с ID {} не найден", userId, filmId);
            throw new ObjectNotFoundException("Нет такого ID");
        }
        likesDbStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (!filmStorage.isValid(filmId) || !userStorage.isExist(userId)) {
            log.warn("Не удалось удалить лайк пользователя с ID {} с фильма с ID {}", userId, filmId);
            throw new ObjectNotFoundException("Нет такого ID");
        }
        likesDbStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = (List<Film>) filmStorage.getFilms();
        films.sort(Comparator.comparingInt(film -> likesDbStorage.getLikes(film.getId()).size()));
        Collections.reverse(films);
        return films.subList(0, Math.min(films.size(), count));
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        if (film == null) {
            log.warn("Нельзя сохранить пустой фильм");
            throw new ValidationException("Некорректный ввод");
        }
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        if (!filmStorage.isValid(id)) {
            log.warn("Некорректный ID");
            throw new ObjectNotFoundException("Нет такого ID");
        }
        return filmStorage.getFilmById(id);
    }
}