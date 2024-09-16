package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void saveResume(Resume r, int index) {
        for (int i = size; i > index; i--) {
            storage[i] = storage[i - 1];
        }
        storage[index] = r;
    }

    @Override
    public void deleteResume(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
    }
}