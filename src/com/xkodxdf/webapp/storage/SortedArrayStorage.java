package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object getSearchKey(Resume r) {
        return Arrays.binarySearch(storage, 0, size, r);
    }

    @Override
    protected void insertElement(Resume r, int index) {
        int insertIdx = -index - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
        size++;
    }

    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
        }
    }
}
