package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.dao.GenreDao;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Optional<Genre> getGenre(Integer id) {
        return genreDao.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }
}
