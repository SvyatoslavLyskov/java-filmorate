package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Slf4j
@Getter
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static long id = 1;
    UserStorage userStorage;

    @Override
    public User createUser(User user) {
        if (isValid(user)) {
            user.setId(id);
            correctName(user);
            users.put(id, user);
            id++;
            log.info("Пользователь создан {}", user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            correctName(user);
            users.put(user.getId(), user);
            log.info("пользователь обновлен");
        } else {
            log.warn("нет такого пользователя");
            throw new ObjectNotFoundException("нет такого пользователя");
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь с id" + id + " не найден.");
        }
        log.info("пользователь с id = {} отправлен", id);
        return users.get(id);
    }

    @Override
    public boolean isExist(long id) {
        return userStorage.isExist(id);
    }

    private boolean isValid(User user) {
        if (users.containsKey(user.getId())) {
            log.warn("Такой пользователь уже существует.");
            throw new ValidationException("Такой пользователь уже существует.");
        } else if (isBlank(user.getLogin())) {
            log.warn("логин не может быть пустым");
            throw new ValidationException("логин не может быть пустым");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("день рождения не может быть в будущем");
            throw new ValidationException("день рождения не может быть в будущем");
        } else if (isBlank(user.getEmail())) {
            log.warn("email не может быть пуст");
            throw new ValidationException("email не может быть пуст");
        } else if (!user.getEmail().contains("@")) {
            log.warn("некорректный email");
            throw new ValidationException("некорректный email");
        } else if (user.getLogin().contains(" ")) {
            log.warn("некорректный login");
            throw new ValidationException("некорректный login");
        } else {
            return true;
        }
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    private void correctName(User user) {
        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }
    }
}
