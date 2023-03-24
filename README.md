# java-filmorate
ER диаграмма базы данных filmorate

<image src="Entity Relationship Diagram.jpg" alt="ER диаграмма"/>

#### FilmService

addFilm(Film film):
``` sql
INSERT INTO film (title, description, release_date, duration, mpa_rating, genre_id)
VALUES (film.getName(), film.getDescription(), ...);
```

getFilm(long id):

``` sql
SELECT * 
FROM film
WHERE film_id = id;
```

getFilms():

``` sql
SELECT * 
FROM film;
```

updateFilm(Film film)

``` sql
UPDATE film
SET title = film.getName(), description = film.getDescription(), ...
WHERE film_id = film.getId();
```

addLike(long userId, long filmId)
``` sql
INSERT INTO like (user_id, film_id)
VALUES (userId, filmId);
```

deleteLike(long userId, long filmId)
``` sql
DELETE FROM like
WHERE film_id = filmId
AND user_Id = userId;
```

#### UserService

addUser(User user):
``` sql
INSERT INTO user (login, email, name, birthday)
VALUES (user.getLogin(), user.getEmail(), ...);
```

getUser(long id):

``` sql
SELECT * 
FROM user
WHERE user_id = id;
```

getUsers():

``` sql
SELECT * 
FROM user;
```

updateUser(User user)

``` sql
UPDATE user
SET login = user.getLogin(), email = user.getEmail(), ...
WHERE user_id = user.getId();
```

addFriend(long userId, long friendId, status)

``` sql
INSERT INTO friend (user_id, friend_id, status)
VALUES (userId, friendId, status);
```

deleteFriend(long userId, long friendId)

``` sql
DELETE FROM friend
WHERE user_id = userId
AND friend_Id = friendId;
```

getFriends(long userId)

``` sql
SELECT * 
FROM friend
WHERE user_id = userId;
```

getCommonFriends(long userId, long friendId)

``` sql
SELECT *
from friend
WHERE user_id = friendId
AND friend_id IN (
                  SELECT friend_id 
                  FROM friend
                  WHERE user_id = userId
                  );
```