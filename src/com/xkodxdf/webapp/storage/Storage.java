package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.model.Resume;

public interface Storage {

    int size();

    void save(Resume r);

    Resume get(String uuid);

    void update(Resume r);

    void delete(String uuid);

    Resume[] getAll();

    void clear();
}
