# java-filmorate
Template repository for Filmorate project.

#### ER диаграмма БД filmorate

![ER диаграмма](\images\Entity Relationship Diagram.jpg "ER диаграмма")
#### FilmSErvice

addFilm(Film film)

```sql
INSERT INTO film (title, description, release_date, ...)
VALUES (film.getTitle(), film.getDescription(), film.getReleaseDate(), ...);
```

getFilm(long filmId)

```sql
SELECT *
FROM film
WHERE film_id = filmId;
```

getFilms()

```sql
SELECT *
FROM film;
```

addLike(long userId, long filmId)

```sql
INSERT INTO like (user_id, film_id)
VALUES (userId, filmId);
```

#### UserService

addUser(User user)

```sql
INSERT INTO user (login, email, name, ...)
VALUES (user.getLogin(), user.getEmail(), user.getName(), ...);
```

getUser(long userId)

```sql
SELECT *
FROM user
WHERE user_id = userId;
```

getUsers()

```sql
SELECT *
FROM user;
```

getFriends(long userId)

```sql
SELECT *
FROM friend
WHERE user_id = userId;
```