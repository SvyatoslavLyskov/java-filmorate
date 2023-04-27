package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Slf4j
@Getter
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static long id = 1;
    UserStorage userStorage;

    public Map<Long, User> getUsers() {
        return users;
    }

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
    public User deleteUserById(Long id) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }

    public User addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long friendId) {
        List<User> mutualFriends = new ArrayList<>();
        for (Long id : getUserById(userId).getFriends()) {
            if (getUserById(friendId).getFriends().contains(id)) {
                mutualFriends.add(getUserById(id));
            }
        }
        return mutualFriends;
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

    private void correctName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
