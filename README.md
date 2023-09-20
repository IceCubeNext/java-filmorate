# java-filmorate

### Описание
Фильмов много — и с каждым годом становится всё больше. Чем их больше, тем больше разных оценок. Чем больше оценок, тем сложнее сделать выбор. 
Для удобства пользователей подготовлен бекенд для сервиса, который будет работать с фильмами и оценками пользователей, а также возвращать топ-5 фильмов, рекомендованных к просмотру. 
Теперь ни вам, ни вашим друзьям не придётся долго размышлять, что посмотреть вечером.

### Стек:
Java 11, REST, Maven, Lombok, Junit5, Mockito, взаимодействие с БД через JDBC

## Ссылка на последний pull-request

[открыть](https://github.com/IceCubeNext/java-filmorate/pull/12)

#### ER диаграмма БД filmorate

<img title="ER diagram" alt="ER diagram" src="/images/Entity Relationship Diagram.jpg">

#### FilmDao (примеры запросов)

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
SELECT f.film_id,
       f.title,
       f.description,
       f.release_date,
       f.duration,
       f.mpa_rating,
       m.name as mpa_name
FROM films AS f
LEFT JOIN mpa_rating AS m ON f.mpa_rating = m.mpa_id
WHERE film_id=?;
```

getFilms():

``` sql
SELECT f.film_id,
       f.title,
       f.description,
       f.release_date,
       f.duration,
       f.mpa_rating,
       m.name as mpa_name
FROM films AS f
LEFT JOIN mpa_rating AS m ON f.mpa_rating = m.mpa_id
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
SELECT f.film_id,
       f.title,
       f.description,
       f.release_date,
       f.duration,
       f.mpa_rating,
       m.name as mpa_name
FROM films AS f
LEFT JOIN mpa_rating AS m ON f.mpa_rating = m.mpa_id
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
       f.mpa_rating,
       m.name as mpa_name
FROM films AS f
LEFT JOIN mpa_rating AS m ON f.mpa_rating = m.mpa_id
LEFT JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY SUM(l.film_id) DESC, f.title
LIMIT(?);
```
