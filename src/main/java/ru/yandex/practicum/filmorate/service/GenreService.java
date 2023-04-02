package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.dao.GenreDao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreService {
    GenreDao genreDao;

    @Autowired
    public GenreService(@Qualifier("GenreDao") GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Optional<Genre> getGenre(Integer id) {
        return genreDao.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }
}
