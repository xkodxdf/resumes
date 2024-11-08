package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.storage.serializer.DataStreamSerializer;

public class DataFileStorageTest extends AbstractStorageTest {
    public DataFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new DataStreamSerializer()));
    }
}
