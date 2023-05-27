package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

public interface GenreStorage {
    void deleteAllGenresForFilm(Film film);

    LinkedHashSet<Genre> getAllGenres();

    Genre getGenreById(long id);

    LinkedHashSet<Genre> getGenresByFilmId(long id);

    boolean hasGenreId(Long id);
}