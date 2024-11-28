package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.Config;
import com.xkodxdf.webapp.ResumeTestData;
import com.xkodxdf.webapp.exception.ExistStorageException;
import com.xkodxdf.webapp.exception.NotExistStorageException;
import com.xkodxdf.webapp.model.Resume;
import org.junit.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    protected final Storage storage;

    protected static final String UUID_1 = UUID.randomUUID().toString();
    protected static final String NAME_1 = "dummy1";
    protected static final String UUID_2 = UUID.randomUUID().toString();
    protected static final String NAME_2 = "dummy2";
    protected static final String UUID_3 = UUID.randomUUID().toString();
    protected static final String NAME_3 = "dummy3";
    protected static final String UUID_4 = UUID.randomUUID().toString();
    protected static final String NOT_EXISTING_RESUME_UUID = UUID.randomUUID().toString();
    protected static final String NAME_5 = "dummycadabra";

    protected static final Resume resume1;
    protected static final Resume resume2;
    protected static final Resume resume3;
    protected static final Resume resume4;
    protected static final Resume notExistingResume;

    static {
        resume1 = ResumeTestData.getTestResume(UUID_1, NAME_1);
        resume2 = ResumeTestData.getTestResume(UUID_2, NAME_2);
        resume3 = ResumeTestData.getTestResume(UUID_3, NAME_3);
        resume4 = ResumeTestData.getTestResume(UUID_4, NAME_3);
        notExistingResume = ResumeTestData.getTestResume(NOT_EXISTING_RESUME_UUID, NAME_5);
    }

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeClass
    public static void makeStorageDir() {
        STORAGE_DIR.mkdir();
    }

    @AfterClass
    public static void deleteStorageDir() {
        STORAGE_DIR.delete();
    }

    @Before
    public void setupStorage() {
        storage.clear();
        storage.save(resume4);
        storage.save(resume2);
        storage.save(resume1);
        storage.save(resume3);
    }

    @After
    public void resetStorage() {
        storage.clear();
    }

    @Test
    public void size() {
        int currentSize = 4;
        assertSize(currentSize);
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

    @Test
    public void getExistingResume() {
        assertGet(resume1);
        assertGet(resume2);
        assertGet(resume3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistingResume() {
        storage.get(NOT_EXISTING_RESUME_UUID);
    }

    @Test
    public void updateExistingResume() {
        Resume newResume = new Resume(UUID_1, "New Name");
        assertNotEquals(newResume, storage.get(UUID_1));
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistingResume() {
        storage.update(notExistingResume);
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
        storage.delete(NOT_EXISTING_RESUME_UUID);
    }

    @Test
    public void getAllSorted() {
        Resume[] expected = {resume1, resume2, resume3, resume4};
        List<Resume> actual = storage.getAllSorted();
        assertEquals(Arrays.asList(expected), actual);
    }

    @Test
    public void clear() {
        int sizeAfterClear = 0;
        Resume[] arrAfterClear = new Resume[0];
        storage.clear();
        assertSize(sizeAfterClear);
        assertArrayEquals(arrAfterClear, storage.getAllSorted().toArray(new Resume[0]));
    }


    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}