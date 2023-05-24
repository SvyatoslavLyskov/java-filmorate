package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public void addFriend(User user1, User user2) {
        if (isExist(user1.getId()) || isExist(user2.getId())) {
            log.warn("Не удалось добавить дружбу");
            throw new ObjectNotFoundException("Нет такого ID");
        }
        friendshipStorage.addFriend(user1, user2);
    }

    public void deleteFriend(User user1, User user2) {
        if (isExist(user1.getId()) || isExist(user2.getId())) {
            log.warn("Не удалось удалить пользователя из друзей");
            throw new ObjectNotFoundException("Нет такого ID");
        }
        friendshipStorage.removeFriend(user1, user2);
    }

    public List<User> getFriendsListById(Long id) {
        if (!isExist(id)) {
            log.info("Получен список друзей пользователя с id{}", userStorage.getUserById(id).getName());
            return friendshipStorage.getFriendsById(id);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public List<User> getMutualFriends(User user1, User user2) {
        if (isExist(user1.getId()) || isExist(user2.getId())) {
            log.warn("Не удалось составить список общих друзей");
            throw new ObjectNotFoundException("Нет такого ID");
        }
        Set<Long> mutual = new HashSet<>(friendshipStorage.getFriendsIdsById(user1.getId()));
        mutual.retainAll(friendshipStorage.getFriendsIdsById(user2.getId()));
        List<User> friendsList = new ArrayList<>();
        for (Long friendId : mutual) {
            User friend = userStorage.getUserById(friendId);
            if (friend != null) {
                friendsList.add(friend);
            } else {
                throw new ObjectNotFoundException("Нет такого ID");
            }
        }
        return friendsList;
    }

    public boolean isExist(Long id) {
        return !userStorage.isExist(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (isExist(user.getId())) {
            log.warn("Не удалось обновить пользователя");
            throw new ObjectNotFoundException("Нет такого ID");
        }
        return userStorage.updateUser(user);
    }

    public User getUserById(Long id) {
        if (isExist(id)) {
            log.warn("Не удалось получить пользователя с ID {}", id);
            throw new ObjectNotFoundException("Нет такого ID");
        }
        return userStorage.getUserById(id);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }
}