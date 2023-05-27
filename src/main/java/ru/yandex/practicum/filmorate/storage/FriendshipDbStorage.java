package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;
    private UserStorage userStorage;
    private final RowMapper<User> rowMapper = (resultSet, rowNum) ->
            userStorage.getUserById(resultSet.getLong("friend_id"));

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getFriendsById(long id) {
        String sql = "SELECT * FROM user_friends WHERE user_id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    @Override
    public List<Long> getFriendsIdsById(long id) {
        List<Long> result = new ArrayList<>();
        for (User user : getFriendsById(id)) {
            result.add(user.getId());
        }
        return result;
    }

    @Override
    public void addFriend(User user, User friend) {
        String sql = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getId(), friend.getId());
    }

    @Override
    public void removeFriend(User user, User exFriend) {
        String sql = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, user.getId(), exFriend.getId());
    }

}