package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private GenreStorage genreDbStorage;
    private MpaStorage mpaDbStorage;
    private final RowMapper<Film> rowMapper = (resultSet, rowNum) -> {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString(("description")));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setGenres(genreDbStorage.getGenresByFilmId(film.getId()));
        film.setMpa(mpaDbStorage.getMpaById(resultSet.getLong("mpa_id")));
        return film;
    };

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreDbStorage, MpaStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());
        sql = "SELECT * FROM films WHERE name = ? ORDER BY id DESC LIMIT 1";
        Film resultFilm = jdbcTemplate.queryForObject(sql, rowMapper, film.getName());
        if (film.getGenres() != null && resultFilm != null) {
            for (Genre genre : new HashSet<>(film.getGenres())) {
                addGenreToFilm(resultFilm, genre);
            }
        }
        resultFilm = jdbcTemplate.queryForObject(sql, rowMapper, film.getName());
        return resultFilm;
    }


    @Override
    public Film updateFilm(Film film) {
        if (!isValid(film.getId())) {
            throw new ObjectNotFoundException("Нет фильма с таким ID");
        }
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        genreDbStorage.deleteAllGenresForFilm(film);

        if (film.getGenres() != null) {
            for (Genre genre : new HashSet<>(film.getGenres())) {
                addGenreToFilm(film, genre);
            }
        }
        sql = "SELECT * FROM films WHERE id = ? ";
        return jdbcTemplate.queryForObject(sql, rowMapper, film.getId());
    }


    @Override
    public Collection<Film> getFilms() {
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, rowMapper);
        return new ArrayList<>(films);
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public boolean isValid(Long id) {
        String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    private void addGenreToFilm(Film film, Genre genre) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, film.getId(), genre.getId());
    }
}
