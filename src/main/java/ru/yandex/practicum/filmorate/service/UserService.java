package ru.yandex.practicum.filmorate.service;

import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j

public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> addFriend(Long firstId, Long secondId) {
        if (isExist(firstId) && isExist(secondId)) {
            if (userStorage.getUserById(firstId).getFriends().contains(secondId)) {
                throw new InternalException("Пользователи уже в списке друзей");
            }
            userStorage.getUserById(firstId).getFriends().add(secondId);
            userStorage.getUserById(secondId).getFriends().add(firstId);
            log.info("Пользователи {}, {} добавлены в список друзей", userStorage.getUserById(firstId).getName(),
                    userStorage.getUserById(secondId).getName());
        }
        return Arrays.asList(userStorage.getUserById(firstId), userStorage.getUserById(secondId));
    }

    public List<User> deleteFriend(Long firstId, Long secondId) {
        if (isExist(firstId) && isExist(secondId)) {
            if (!userStorage.getUserById(firstId).getFriends().contains(secondId)) {
                throw new InternalException("Пользователи не в списке друзей");
            }
            userStorage.getUserById(firstId).getFriends().remove(secondId);
            userStorage.getUserById(secondId).getFriends().remove(firstId);
            log.info("Пользователи {} и {} удалены из списка друзей", userStorage.getUserById(firstId).getName(),
                    userStorage.getUserById(secondId).getName());
        }
        return Arrays.asList(userStorage.getUserById(firstId), userStorage.getUserById(secondId));
    }

    public List<User> getFriendsListById(Long id) {
        if (isExist(id)) {
            log.info("Получен список друзей пользователя с id{}", userStorage.getUserById(id).getName());

            return userStorage.getUserById(id).getFriends().stream()
                    .map(userStorage::getUserById)
                    .collect(Collectors.toList());
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public List<User> getMutualFriends(Long firstId, Long secondId) {
        return userStorage.getMutualFriends(firstId, secondId);
    }

    private boolean isExist(Long id) {
        return userStorage.getUserById(id) != null;
    }

    public User deleteUserById(Long id) {
        if (isExist(id)) {
            log.info("Пользователь с id {} удален", id);
            return userStorage.deleteUserById(id);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(Long id) {
        if (isExist(id)) {
            return userStorage.getUserById(id);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public Map<Long, User> getUsers() {
        return userStorage.getUsers();
    }
}