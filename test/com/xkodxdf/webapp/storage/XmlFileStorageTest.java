package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.storage.serializer.XmlStreamSerializer;

public class XmlFileStorageTest extends AbstractStorageTest {

    public XmlFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new XmlStreamSerializer()));
    }
}
