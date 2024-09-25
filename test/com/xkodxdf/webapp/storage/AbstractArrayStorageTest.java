package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.exception.StorageException;
import com.xkodxdf.webapp.model.Resume;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;


public abstract class AbstractArrayStorageTest {

    protected Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String NOT_EXISTING_RESUME = "abracadabra";
    private final Resume resume1 = new Resume(UUID_1);
    private final Resume resume2 = new Resume(UUID_2);
    private final Resume resume3 = new Resume(UUID_3);
    private final Resume notExistingResume = new Resume(NOT_EXISTING_RESUME);


    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }


    @Before
    public void setUpStorage() throws Exception {
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @After
    public void resetStorage() throws Exception {
        storage.clear();
    }


    @Test
    public void testSize() throws Exception {
        int currentSize = 3;
        assertEquals(currentSize, storage.size());
    }

    @Test
    public void testClearSizeReset() throws Exception {
        int sizeAfterClear = 0;
        assertNotEquals(sizeAfterClear, storage.size());
        storage.clear();
        assertEquals(sizeAfterClear, storage.size());
    }

    @Test
    public void testClearStorageContentReset() throws NoSuchFieldException, IllegalAccessException {
        Resume[] storageArr = getStorageArray();
        assertFalse(isArrContainsOnlyNull(storageArr));
        storage.clear();
        assertTrue(isArrContainsOnlyNull(storageArr));
    }

    @Test
    public void testUpdateExistingResume() throws Exception {
        Resume newResume = new Resume(UUID_1);
        assertNotSame(newResume, storage.get(UUID_1));
        storage.update(newResume);
        assertSame(newResume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void testUpdateNotExistingResume() {
        storage.update(notExistingResume);
    }

    @Test
    public void testGetAll() throws Exception {
        Resume[] expected = {resume1, resume2, resume3};
        Resume[] actual = storage.getAll();
        assertArrayEquals(expected, actual);
    }

    @Test(expected = ExistStorageException.class)
    public void testSaveExistingResume() {
        storage.save(resume1);
    }

    @Test
    public void testSaveNotExistingResume() throws Exception {
        int sizeBeforeSave = storage.size();
        int sizeAfterSave = sizeBeforeSave + 1;
        storage.save(notExistingResume);
        assertEquals(notExistingResume, storage.get(NOT_EXISTING_RESUME));
        assertEquals(sizeAfterSave, storage.size());
    }

    @Test(expected = StorageException.class)
    public void testSaveToFullFilledStorage() {
        try {
            while (storage.size() < AbstractArrayStorage.STORAGE_LIMIT) {
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
        int storageSizeAfterDeleting = storageSizeBeforeDeleting - 1;

        storage.delete(UUID_1);
        assertEquals(storageSizeAfterDeleting, storage.size());
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void testDeleteNotExistingResume() {
        storage.delete(NOT_EXISTING_RESUME);
    }

    @Test
    public void getExistingResume() throws Exception {
        Resume testResume = storage.get(UUID_1);
        assertEquals(resume1, testResume);
    }

    @Test(expected = NotExistStorageException.class)
    public void testGetNotExistingResume() {
        storage.get(NOT_EXISTING_RESUME);
    }


    protected Resume[] getStorageArray() throws NoSuchFieldException, IllegalAccessException {
        Class<?> storageSuperClass = storage.getClass().getSuperclass();
        Field storageField = storageSuperClass.getDeclaredField("storage");
        storageField.setAccessible(true);
        return (Resume[]) storageField.get(storage);
    }

    private boolean isArrContainsOnlyNull(Resume[] arr) {
        for (Resume r : arr) {
            if (r != null) {
                return false;
            }
        }

        return true;
    }
}