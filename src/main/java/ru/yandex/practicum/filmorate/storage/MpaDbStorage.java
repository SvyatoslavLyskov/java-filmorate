package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private List<Mpa> mpas;
    private final RowMapper<Mpa> rowMapper = (resultSet, rowNum) -> {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    };

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpas() {
        if (mpas == null) {
            fillMpas();
        }
        return mpas;
    }

    @Override
    public boolean hasMpaId(Long id) {
        if (mpas == null) {
            fillMpas();
        }
        for (Mpa mpa : mpas) {
            if (mpa.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void fillMpas() {
        String sql = "SELECT * FROM mpas";
        mpas = jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Mpa getMpaById(Long id) {
        if (mpas == null) {
            fillMpas();
        }
        for (Mpa mpa : mpas) {
            if (mpa.getId() == id)
                return mpa;
        }
        return null;
    }
}
