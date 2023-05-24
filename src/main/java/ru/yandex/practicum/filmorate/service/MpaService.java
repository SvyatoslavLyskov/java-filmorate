package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MpaService {

    private final MpaStorage mpaDbStorage;

    public List<Mpa> getAllMpas() {
        return mpaDbStorage.getAllMpas();
    }

    public boolean hasMpaId(Long id) {
        return mpaDbStorage.hasMpaId(id);
    }

    public Mpa getMpaId(Long id) {
        if (!hasMpaId(id)) {
            log.warn("Не удалось получить MPA с таким ID");
            throw new ObjectNotFoundException("Нет такого ID");
        }
        return mpaDbStorage.getMpaById(id);
    }
}