package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static int id = 1;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.warn("Такой пользователь уже существует.");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Такой пользователь уже существует.");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("логин не может быть пустым");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "логин не может быть пустым");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("день рождения не может быть в будущем");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "день рождения не может быть в будущем");
        } else if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("email не может быть пуст");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "email не может быть пуст");
        }else if (!user.getEmail().contains("@")) {
            log.warn("некорректный email");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "некорректный email");
        }else if (user.getLogin().contains(" ")){
            log.warn("некорректный login");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "некорректный login");
        }else{
            user.setId(id);
            correctName(user);
            users.put(id, user);
            id++;
            log.warn("Пользователь создан {}", user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            correctName(user);
            users.put(user.getId(), user);
            log.info("пользователь обновлен");
        } else {
            log.warn("нет такого пользователя");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "нет такого пользователя");
        }
        return user;
    }

    private void correctName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}