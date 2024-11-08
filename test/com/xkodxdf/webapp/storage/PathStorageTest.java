package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.storage.serializer.ObjectStreamSerializer;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerializer()));
    }
}