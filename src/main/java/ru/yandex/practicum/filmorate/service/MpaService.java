package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.dao.MpaDao;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MpaService {
    MpaDao mpaDao;

    @Autowired
    public MpaService(@Qualifier("MpaDao") MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }
    public Optional<Mpa> getMpa(Integer id) {
        return mpaDao.getMpaById(id);
    }

    public List<Mpa> getMpaTypes() {
        return mpaDao.getMpaTypes().stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
    }
}
