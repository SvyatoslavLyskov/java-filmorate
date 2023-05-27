create TABLE IF NOT EXISTS mpas
(
    id   IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

create TABLE IF NOT EXISTS films
(
    id           IDENTITY PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    release_date DATE,
    duration     INT,
    mpa_id       BIGINT,
    FOREIGN KEY (mpa_id) REFERENCES mpas (id)
);

create TABLE IF NOT EXISTS genres
(
    id   IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

create TABLE IF NOT EXISTS film_genres
(
    film_id  BIGINT,
    genre_id BIGINT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);

create TABLE IF NOT EXISTS users
(
    id       IDENTITY PRIMARY KEY,
    login    VARCHAR(255) NOT NULL UNIQUE,
    name     VARCHAR(255),
    email    VARCHAR(255) NOT NULL UNIQUE,
    birthday DATE
);

create unique index IF NOT EXISTS USER_EMAIL_UINDEX ON USERS (email);

create unique index IF NOT EXISTS USER_LOGIN_UINDEX ON USERS (login);

create TABLE IF NOT EXISTS film_likes
(
    film_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

create TABLE IF NOT EXISTS user_friends
(
    user_id   BIGINT,
    friend_id BIGINT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id)
);