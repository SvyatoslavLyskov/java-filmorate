package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    Map<Long, User> getUsers();

    User getUserById(Long id);

    User deleteUserById(Long id);

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId);

    List<User> getMutualFriends(Long userId, Long friendId);
}
