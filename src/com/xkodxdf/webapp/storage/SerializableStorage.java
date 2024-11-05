package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializableStorage {

    Resume doRead(InputStream is) throws IOException;

    void doWrite(Resume r, OutputStream os) throws IOException;
}
