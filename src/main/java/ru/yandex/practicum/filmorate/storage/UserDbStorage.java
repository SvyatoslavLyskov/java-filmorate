package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = (resultSet, rowNum) -> {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    };

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (login, name, email, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        sql = "SELECT * FROM users WHERE login = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, user.getLogin());
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        sql = "SELECT * FROM users WHERE login = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, user.getLogin());
    }

    @Override
    public ArrayList<User> getUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, rowMapper);
        return new ArrayList<>(users);
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public boolean isExist(long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
}
