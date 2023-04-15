package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    private final UserController controller = new UserController();

    @Test
    public void shouldNotCreateUserWithEmptyLogin() {
        User user = new User();
        user.setName("Andrei");
        user.setLogin("");
        user.setEmail("mail@yandex.ru");
        user.setBirthday(LocalDate.of(1985, 6, 15));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    public void shouldNotCreateUserWithBadLogin() {
        User user = new User();
        user.setName("Andrew");
        user.setLogin("Andy b");
        user.setEmail("mail@yandex.ru");
        user.setBirthday(LocalDate.of(1992, 11, 10));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("некорректный login", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithNullEmail() {
        User user = new User();
        user.setName("Павел");
        user.setLogin("Pavel");
        user.setBirthday(LocalDate.of(1975, 11, 12));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("email не может быть пуст", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithBadEmail() {
        User user = new User();
        user.setName("Alexander");
        user.setLogin("Yablonsky10");
        user.setEmail("alexmail.ru");
        user.setBirthday(LocalDate.of(1991, 5, 15));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("некорректный email", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithBlankEmail() {
        User user = new User();
        user.setName("George");
        user.setLogin("Bush");
        user.setEmail("");
        user.setBirthday(LocalDate.of(1990, 11, 10));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("email не может быть пуст", exception.getMessage());
    }

    @Test
    public void shouldNotCreateUserWithBadBirthday() {
        User user = new User();
        user.setName("Nataly");
        user.setLogin("login7");
        user.setEmail("mail@yandex.ru");
        user.setBirthday(LocalDate.of(2028, 5, 16));
        assertThrows(ValidationException.class, () -> controller.createUser(user));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () ->
                controller.createUser(user));
        Assertions.assertEquals("день рождения не может быть в будущем", exception.getMessage());
    }
}