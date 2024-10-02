package com.xkodxdf.webapp.storage.array_storage;

import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.abstract_storage.AbstractArrayStorage;

import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void fillDeletedElement(int index) {
        storage[index] = storage[size - 1];
    }

    @Override
    protected void insertElement(Resume r, int index) {
        storage[size] = r;
        size++;
    }

    @Override
    protected int getIndex(Resume r) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(r.getUuid(), (storage[i].getUuid()))) {
                return i;
            }
        }
        return -1;
    }
}
