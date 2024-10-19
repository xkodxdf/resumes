package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public final void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r);
        doSave(r, searchKey);
    }

    @Override
    public final Resume get(String uuid) {
        Object searchKey = getExistingSearchKey(new Resume(uuid, "dummy"));
        return doGet(searchKey);
    }

    @Override
    public void update(Resume r) {
        Object searchKey = getExistingSearchKey(r);
        doUpdate(r, searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getExistingSearchKey(new Resume(uuid, "dummy"));
        doDelete(searchKey);
    }


    private Object getExistingSearchKey(Resume r) {
        Object searchKey = getSearchKey(r.getUuid());
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(r.getUuid());
        }
        return searchKey;
    }

    private Object getNotExistingSearchKey(Resume r) {
        Object searchKey = getSearchKey(r.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(r.getUuid());
        }
        return searchKey;
    }

    protected abstract boolean isExist(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void doSave(Resume r, Object searchKey);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doUpdate(Resume r, Object searchKey);

    protected abstract void doDelete(Object searchKey);

}