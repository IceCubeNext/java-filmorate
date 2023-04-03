#### ER диаграмма БД filmorate

<img title="ER diagram" alt="ER diagram" src="/images/Entity Relationship Diagram.jpg">

#### FilmDao

containsFilm(long id):

``` sql
SELECT * 
FROM FILMS 
WHERE film_id=?;
```

addFilm(Film film):

``` sql
INSERT INTO films (title, description, release_date, duration, mpa_rating)
VALUES (?, ?, ?, ?, ?);
```

getFilmById(long id):

``` sql
SELECT * 
FROM films
WHERE film_id = id;
```

getFilms():

``` sql
SELECT * 
FROM films
ORDER BY film_id;
```

updateFilm(Film film)

``` sql
UPDATE films 
SET title = ?, description = ?, release_date = ?, duration = ?, mpa_rating = ?
WHERE film_id = ?;
```

deleteFilm(long id)

``` sql
DELETE FROM films
WHERE film_id = ?;
```

#### UserDao

containsUser(Long id):
``` sql
SELECT * FROM users
WHERE user_id=?;
```

addUser(User user):
``` sql
INSERT INTO users (login, email, name, birthday)
VALUES (?, ?, ?, ?);
```

getUserById(Long id):

``` sql
SELECT * FROM users
WHERE user_id=?;
```

getUsers():

``` sql
SELECT * FROM users
ORDER BY user_id;
```

updateUser(User user)

``` sql
UPDATE users
SET login = ?, email = ?, name = ?, birthday = ?
WHERE user_id = ?;
```

deleteUser(Long id)

``` sql
DELETE FROM users
WHERE user_id = ?;
```

#### Some others

getUsersFavoriteFilms(Long id)

``` sql
SELECT * FROM films
WHERE film_id IN
                (SELECT film_id 
                 FROM likes 
                 WHERE user_id = ?);
```

getFilmFollowers(Long id)

``` sql
SELECT * FROM users
WHERE user_id IN
                (SELECT user_id 
                 FROM likes 
                 WHERE film_id = ?);
```

getTop(int count):

``` sql
SELECT f.film_id,
       f.title,
       f.description,
       f.release_date,
       f.duration,
       f.mpa_rating FROM films AS f
LEFT JOIN likes AS l ON f.film_id = l.film_id
ORDER BY SUM(l.film_id) DESC, f.title
LIMIT(?);
```
