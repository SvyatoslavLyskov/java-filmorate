package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    private final UserController controller = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    public void shouldNotCreateUserWithEmptyLogin() {
        User user = new User(1, "mail@yandex.ru", "", "Andrew",
                LocalDate.of(1985, 6, 15));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    public void shouldNotCreateUserWithBadLogin() {
        User user = new User(1, "mail@yandex.ru", "Andy A", "Andrew",
                LocalDate.of(1985, 6, 15));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("некорректный login", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithNullEmail() {
        User user = new User(1, null, "best", "Andrew",
                LocalDate.of(1985, 6, 15));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("email не может быть пуст", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithBadEmail() {
        User user = new User(1, "andrewmail.ru", "best", "Andrew",
                LocalDate.of(1985, 6, 15));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("некорректный email", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithBlankEmail() {
        User user = new User(1, "", "best", "Andrew",
                LocalDate.of(1985, 6, 15));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("email не может быть пуст", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithBadBirthday() {
        User user = new User(1, "mail@yandex.ru", "best", "Andrew",
                LocalDate.of(2028, 5, 16));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("день рождения не может быть в будущем", exception.getMessage());
    }
}