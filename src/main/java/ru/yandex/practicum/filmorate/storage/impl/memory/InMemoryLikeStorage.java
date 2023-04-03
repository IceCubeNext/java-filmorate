package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryLikeStorage")
public class InMemoryLikeStorage implements LikeStorage {
    FilmStorage filmStorage;
    UserStorage userStorage;
    Map<Long, Set<Long>> filmLikes = new HashMap<>();
    Map<Long, Set<Long>> userLikes = new HashMap<>();

    public InMemoryLikeStorage(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage,
                               @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public boolean addLike(Long id, Long userId) {
        if (filmStorage.containsFilm(id) && userStorage.containsUser(userId)) {
            if (filmLikes.containsKey(id)) {
                filmLikes.get(id).add(userId);
            } else {
                filmLikes.put(id, new HashSet<>());
                filmLikes.get(id).add(userId);
            }

            if (userLikes.containsKey(userId)) {
                userLikes.get(userId).add(id);
            } else {
                userLikes.put(userId, new HashSet<>());
                userLikes.get(userId).add(id);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        if (filmStorage.containsFilm(id) && userStorage.containsUser(userId)) {
            if (filmLikes.containsKey(id)) {
                filmLikes.get(id).remove(userId);
            }

            if (userLikes.containsKey(userId)) {
                userLikes.get(userId).remove(id);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Film> getUsersFavoriteFilms(Long id) {
        if (userLikes.containsKey(id)) {
            return userLikes.get(id).stream()
                    .map(filmStorage::getFilmById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<User> getFilmFollowers(Long id) {
        if (filmLikes.containsKey(id)) {
            return filmLikes.get(id).stream()
                    .map(userStorage::getUserById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Film> getTop(Integer count) {
        return filmLikes.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getValue().size() * -1))
                .limit(count)
                .map(Map.Entry::getKey)
                .map(filmStorage::getFilmById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public boolean deleteUser(Long id) {
        if (userLikes.containsKey(id)) {
            for(Long filmId: userLikes.get(id)) {
                if(filmLikes.containsKey(filmId)) {
                    filmLikes.get(filmId).remove(id);
                    if (filmLikes.get(filmId).size() == 0) filmLikes.remove(filmId);
                }
            }
            userLikes.remove(id);
            return true;
        }
        return false;
    }

    public boolean deleteFilm(Long id) {
        if (filmLikes.containsKey(id)) {
            for(Long userId: filmLikes.get(id)) {
                if(userLikes.containsKey(userId)) {
                    userLikes.get(userId).remove(id);
                    if (userLikes.get(userId).size() == 0) userLikes.remove(userId);
                }
            }
            filmLikes.remove(id);
            return true;
        }
        return false;
    }
}
