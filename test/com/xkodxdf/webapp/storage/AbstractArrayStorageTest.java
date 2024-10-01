package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.exception.StorageException;
import com.xkodxdf.webapp.model.Resume;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public abstract class AbstractArrayStorageTest {

    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String NOT_EXISTING_RESUME = "abracadabra";
    private static final Resume resume1;
    private static final Resume resume2;
    private static final Resume resume3;
    private static final Resume notExistingResume;


    static {
        resume1 = new Resume(UUID_1);
        resume2 = new Resume(UUID_2);
        resume3 = new Resume(UUID_3);
        notExistingResume = new Resume(NOT_EXISTING_RESUME);
    }


    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setupStorage() throws Exception {
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @After
    public void resetStorage() throws Exception {
        storage.clear();
    }


    @Test
    public void size() throws Exception {
        int currentSize = 3;
        assertSize(currentSize);
    }

    @Test
    public void clear() throws Exception {
        int sizeAfterClear = 0;
        Resume[] arrAfterClear = new Resume[0];
        storage.clear();
        assertSize(sizeAfterClear);
        assertArrayEquals(arrAfterClear, storage.getAll());
    }

    @Test
    public void updateExistingResume() throws Exception {
        Resume newResume = new Resume(UUID_1);
        assertNotSame(newResume, storage.get(UUID_1));
        storage.update(newResume);
        assertSame(newResume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistingResume() {
        storage.update(notExistingResume);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expected = {resume1, resume2, resume3};
        Resume[] actual = storage.getAll();
        assertArrayEquals(expected, actual);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistingResume() {
        storage.save(resume1);
    }

    @Test
    public void saveNotExistingResume() throws Exception {
        int sizeBeforeSave = storage.size();
        storage.save(notExistingResume);
        assertGet(notExistingResume);
        assertSize(sizeBeforeSave + 1);
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

    @Test(expected = NotExistStorageException.class)
    public void deleteExistingResume() throws Exception {
        int storageSizeBeforeDeleting = storage.size();
        storage.delete(UUID_1);
        assertSize(storageSizeBeforeDeleting - 1);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistingResume() {
        storage.delete(NOT_EXISTING_RESUME);
    }

    @Test
    public void getExistingResume() throws Exception {
        assertGet(resume1);
        assertGet(resume2);
        assertGet(resume3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistingResume() {
        storage.get(NOT_EXISTING_RESUME);
    }


    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}