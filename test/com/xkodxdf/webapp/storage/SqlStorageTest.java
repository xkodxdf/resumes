package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(Config.get().getSqlStorage());
    }
}
