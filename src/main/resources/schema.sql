CREATE TABLE IF NOT EXISTS mpa_rating
(
    mpa_id int generated by default as identity PRIMARY KEY,
    name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    film_id int generated by default as identity PRIMARY KEY,
    title varchar(64) NOT NULL,
    description varchar(200),
    release_date timestamp,
    duration int NOT NULL,
    mpa_rating int,
    CONSTRAINT fk_mpa_rating
        FOREIGN KEY (mpa_rating)
            REFERENCES mpa_rating (mpa_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS film_id_index ON films (film_id);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id int generated by default as identity PRIMARY KEY,
    name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id int,
    genre_id int,
    PRIMARY KEY (film_id, genre_id),
    CONSTRAINT fk_film
        FOREIGN KEY (film_id)
            REFERENCES films (film_id),
    CONSTRAINT fk_film_genre
        FOREIGN KEY (genre_id)
            REFERENCES genre (genre_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id int generated by default as identity PRIMARY KEY,
    login varchar(64) NOT NULL UNIQUE,
    email varchar(64) NOT NULL UNIQUE,
    name varchar(64) NOT NULL,
    birthday timestamp
);

CREATE UNIQUE INDEX IF NOT EXISTS user_id_index ON users (user_id);

CREATE TABLE IF NOT EXISTS likes
(
    user_id int,
    film_id int,
    PRIMARY KEY (user_id, film_id),
    CONSTRAINT fk_like_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    CONSTRAINT fk_like_film
        FOREIGN KEY (film_id)
            REFERENCES films (film_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id int,
    friend_id int,
    status int,
    PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_friend_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    CONSTRAINT fk_friend
        FOREIGN KEY (friend_id)
            REFERENCES users (user_id),
    CONSTRAINT status_valid_values CHECK (status IN (0, 1, 2))
);