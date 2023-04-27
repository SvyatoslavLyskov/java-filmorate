package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService = new UserService(userStorage);

    @Test
    public void shouldCreateUser() {
        User user = new User(1, "mail2@yandex.ru", "best2", "Andrew2",
                LocalDate.of(1990, 5, 16));
        userService.createUser(user);
        assertEquals(user, userService.getUserById(user.getId()));
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User(1, "mail@yandex.ru", "best", "Andrew",
                LocalDate.of(1999, 5, 16));
        user.setEmail("mail@yandex.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userService.createUser(user);
        User userUpd = new User(1, "mail2@yandex.ru", "best2", "Andrew2",
                LocalDate.of(1990, 5, 16));
        userService.updateUser(userUpd);
        assertEquals(userUpd, userService.updateUser(userUpd));
    }

    @Test
    public void shouldNotUpdateUser() {
        User user = new User(1, "mail@yandex.ru", "best", "Andrew",
                LocalDate.of(2000, 5, 16));
        assertThrows(ObjectNotFoundException.class, () -> userService.updateUser(user));
        assertTrue(userService.getUsers().isEmpty());
    }
}