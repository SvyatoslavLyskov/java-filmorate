package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.LinkedHashSet;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GenreService {

    private final GenreStorage genreDbStorage;

    public LinkedHashSet<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public boolean hasGenreId(Long id) {
        return genreDbStorage.hasGenreId(id);
    }

    public Genre getGenreId(Long id) {
        if (!hasGenreId(id)) {
            log.warn("Не удалось получить жанр с таким ID");
            throw new ObjectNotFoundException("Нет такого ID");
        }
        return genreDbStorage.getGenreById(id);
    }

}