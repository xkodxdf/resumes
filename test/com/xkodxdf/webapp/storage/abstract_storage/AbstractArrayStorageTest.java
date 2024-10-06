package com.xkodxdf.webapp.storage.abstract_storage;

import com.xkodxdf.webapp.exception.StorageException;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.Storage;
import org.junit.Test;

import static org.junit.Assert.*;


public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }


    @Test(expected = StorageException.class)
    public void saveStorageOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            System.out.println("the overflow occurred ahead of time");
            fail();
        }
        storage.save(new Resume());
    }
}