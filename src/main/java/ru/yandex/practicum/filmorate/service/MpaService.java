package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.dao.MpaDao;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Optional<Mpa> getMpa(Integer id) {
        return mpaDao.getMpaById(id);
    }

    public List<Mpa> getMpaTypes() {
        return mpaDao.getMpaTypes();
    }
}
