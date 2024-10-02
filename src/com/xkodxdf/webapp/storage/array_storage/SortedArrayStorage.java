package com.xkodxdf.webapp.storage.array_storage;

import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.abstract_storage.AbstractArrayStorage;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
        }
    }

    @Override
    protected void insertElement(Resume r, int index) {
        int insertIdx = -index - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
        size++;
    }

    @Override
    protected int getIndex(Resume r) {
        return Arrays.binarySearch(storage, 0, size, r);
    }
}
