package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class FilmorateApplicationTests {
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;
    private final LikeDbStorage likeDbStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUserById(1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void removeLike() {
        likeDbStorage.removeLike(1L, 1L);
        assertThat(likeDbStorage.getLikes(1L).size()).isEqualTo(1);
    }

    @Test
    void testCreateUser() {
        User user = new User(9L, "testLogin99", "testName9", "testEmail99@a.a",
                LocalDate.now().minusYears(35));
        userDbStorage.createUser(user);
        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUserById(9L));
        assertThat(userOptional).isPresent().hasValueSatisfying(u -> {
                    assertThat(u).hasFieldOrPropertyWithValue("id", 9L);
                    assertThat(u).hasFieldOrPropertyWithValue("email", "testEmail99@a.a");
                }
        );
    }

    @Test
    void testUpdateUser() {
        User user = new User(2, "testLogin2", "testName2", "testEmail2@a.a",
                LocalDate.now().minusYears(35));
        User savedUser = userDbStorage.createUser(user);
        assertThat(savedUser).isNotNull();
        user = new User(3, "testLogin3", "testName3", "testEmail3@a.a",
                LocalDate.now().minusYears(30));
        savedUser = userDbStorage.createUser(user);
        savedUser.setLogin("updatedLogin");
        savedUser.setName("updatedName");
        savedUser.setEmail("updatedEmail@a.a");
        savedUser.setBirthday(LocalDate.now().minusYears(25));
        User updatedUser = userDbStorage.updateUser(savedUser);
        assertThat(updatedUser).isEqualTo(savedUser);
    }

    @Test
    void testGetUsers() {
        User user1 = new User(4, "testLogin4", "testName4", "testEmail4@a.a",
                LocalDate.now().minusYears(20));
        User user2 = new User(5, "testLogin5", "testName5", "testEmail5@a.a",
                LocalDate.now().minusYears(15));
        User savedUser1 = userDbStorage.createUser(user1);
        User savedUser2 = userDbStorage.createUser(user2);
        ArrayList<User> allUsers = userDbStorage.getUsers();
        assertThat(allUsers).contains(savedUser1, savedUser2);
    }

    @Test
    void testIsExist() {
        User user = new User(6, "testLogin6", "testName6", "testEmail6@a.a",
                LocalDate.now().minusYears(10));
        User savedUser = userDbStorage.createUser(user);
        boolean isExist = userDbStorage.isExist(savedUser.getId());
        assertThat(isExist).isTrue();
    }

    @Test
    void testAddFriend() {
        User user1 = new User(7, "testLogin7", "testName7", "testEmail7@a.a",
                LocalDate.now().minusYears(90));
        User user2 = new User(8, "testLogin8", "testName8", "testEmail8@a.a",
                LocalDate.now().minusYears(54));
        User savedUser1 = userDbStorage.createUser(user1);
        User savedUser2 = userDbStorage.createUser(user2);
        friendshipDbStorage.addFriend(savedUser1, savedUser2);
        List<User> friends = friendshipDbStorage.getFriendsById(savedUser1.getId());
        List<Long> friendIds = friendshipDbStorage.getFriendsIdsById(savedUser1.getId());
        assertThat(friends).contains(savedUser2);
        assertThat(friendIds).contains(savedUser2.getId());
    }

    @Test
    void testRemoveFriend() {
        User user1 = new User(9, "testLogin9", "testName9", "testEmail9@a.a",
                LocalDate.now().minusYears(90));
        User user2 = new User(10, "testLogin10", "testName10", "testEmail10@a.a",
                LocalDate.now().minusYears(54));
        User savedUser1 = userDbStorage.createUser(user1);
        User savedUser2 = userDbStorage.createUser(user2);
        friendshipDbStorage.removeFriend(savedUser1, savedUser2);
        List<User> friends = friendshipDbStorage.getFriendsById(savedUser1.getId());
        List<Long> friendIds = friendshipDbStorage.getFriendsIdsById(savedUser1.getId());
        assertThat(friends).doesNotContain(savedUser2);
        assertThat(friendIds).doesNotContain(savedUser2.getId());
    }

    @Test
    void testAddGenre() {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = new Genre(1, "Комедия");
        genres.add(genre);
        genre = new Genre(6, "Боевик");
        genres.add(genre);
        Film film = new Film(3L, "testName3", "testDescription3",
                LocalDate.now(), 120, genres, new Mpa(1, "G"));
        Film savedFilm = filmDbStorage.createFilm(film);
        assertThat(film).isEqualTo(savedFilm);
    }

    @Test
    void testRemoveGenre() {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = new Genre(1, "Комедия");
        genres.add(genre);
        genre = new Genre(6, "Боевик");
        genres.add(genre);
        Film film = new Film(2L, "testName2", "testDescription2",
                LocalDate.now(), 120, genres, new Mpa(1, "G"));
        filmDbStorage.createFilm(film);
        Film loadedFilm = filmDbStorage.getFilmById(2L);
        assertThat(film).isEqualTo(loadedFilm);
        film.setDescription("tdupdated");
        genres.remove(1);
        film.setGenres(genres);
        Film updatedFilm = filmDbStorage.updateFilm(film);
        assertThat(film).isEqualTo(updatedFilm);
    }

    @Test
    void testAddLike() {
        Film film = new Film(1L, "testName1", "testDescription1",
                LocalDate.now(), 120, null, new Mpa(1, "G"));
        filmDbStorage.createFilm(film);
        likeDbStorage.addLike(1L, 1L);
        likeDbStorage.addLike(1L, 2L);
        assertThat(likeDbStorage.getLikes(1L).size()).isEqualTo(2);
    }
}
