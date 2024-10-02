package com.xkodxdf.webapp.storage.dynamic_storage;

import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.abstract_storage.AbstractStorage;

import java.util.ArrayList;
import java.util.List;


public class ListStorage extends AbstractStorage {

    private final List<Resume> storage = new ArrayList<>(10_000);


    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }


    @Override
    protected int getIndex(Resume r) {
        return storage.indexOf(r);
    }

    @Override
    protected Resume getElement(int index) {
        return storage.get(index);
    }

    @Override
    protected void updateElement(Resume r, int index) {
        storage.set(index, r);
    }

    @Override
    protected void insertElement(Resume r, int index) {
        storage.add(r);
    }

    @Override
    protected void deleteElement(int index) {
        storage.remove(index);
    }

    @Override
    protected void fillDeletedElement(int index) {
    }
}
