package com.xkodxdf.webapp.storage.abstract_storage;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.Storage;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume r) {
        int index = getIndex(r);
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        }
        updateElement(r, index);
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r);
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        }
        insertElement(r, index);
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(new Resume(uuid));
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getElement(index);
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(new Resume(uuid));
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        fillDeletedElement(index);
        deleteElement(index);
    }


    protected abstract int getIndex(Resume r);

    protected abstract Resume getElement(int index);

    protected abstract void updateElement(Resume r, int index);

    protected abstract void insertElement(Resume r, int index);

    protected abstract void deleteElement(int index);

    protected abstract void fillDeletedElement(int index);
}