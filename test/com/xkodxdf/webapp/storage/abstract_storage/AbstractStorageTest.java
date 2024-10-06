package com.xkodxdf.webapp.storage.abstract_storage;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.Storage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {

    protected final Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String NOT_EXISTING_RESUME = "abracadabra";
    protected static final Resume resume1;
    protected static final Resume resume2;
    protected static final Resume resume3;
    protected static final Resume notExistingResume;


    static {
        resume1 = new Resume(UUID_1);
        resume2 = new Resume(UUID_2);
        resume3 = new Resume(UUID_3);
        notExistingResume = new Resume(NOT_EXISTING_RESUME);
    }


    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setupStorage() {
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @After
    public void resetStorage() {
        storage.clear();
    }


    @Test
    public void size() {
        int currentSize = 3;
        assertSize(currentSize);
    }

    @Test
    public void clear() {
        int sizeAfterClear = 0;
        Resume[] arrAfterClear = new Resume[0];
        storage.clear();
        assertSize(sizeAfterClear);
        assertArrayEquals(arrAfterClear, storage.getAll());
    }

    @Test
    public void updateExistingResume() {
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
    public void getAll() {
        Resume[] expected = {resume1, resume2, resume3};
        assertArrayEquals(expected, storage.getAll());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistingResume() {
        storage.save(resume1);
    }

    @Test
    public void saveNotExistingResume() {
        int sizeBeforeSave = storage.size();
        storage.save(notExistingResume);
        assertGet(notExistingResume);
        assertSize(sizeBeforeSave + 1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExistingResume() {
        int storageSizeBeforeDeleting = storage.size();
        try {
            storage.delete(UUID_1);
        } catch (NotExistStorageException e) {
            Assert.fail();
        }
        assertSize(storageSizeBeforeDeleting - 1);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistingResume() {
        storage.delete(NOT_EXISTING_RESUME);
    }

    @Test
    public void getExistingResume() {
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