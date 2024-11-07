package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.storage.serializable.ObjectStreamSerializer;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}