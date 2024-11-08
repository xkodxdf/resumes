package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.storage.serializer.JsonStreamSerializer;

public class JsonFileStorageTest extends AbstractStorageTest {

    public JsonFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new JsonStreamSerializer()));
    }
}
