package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();


    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }


    @Override
    protected boolean isExist(Object searchKey) {
        return Objects.nonNull(searchKey);
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(((Resume) searchKey).getUuid());
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        storage.replace(((Resume) searchKey).getUuid(), r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(((Resume) searchKey).getUuid());
    }

    @Override
    protected List<Resume> doCopy() {
        return new ArrayList<>(storage.values());
    }
}
