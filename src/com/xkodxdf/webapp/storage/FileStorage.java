package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.StorageException;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.serializable.SerializableStorage;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;
    private SerializableStorage serializer;

    protected FileStorage(File directory, SerializableStorage serializer) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(serializer, "serializer must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
        this.serializer = serializer;
    }

    public void setSerializer(SerializableStorage serializer) {
        this.serializer = serializer;
    }

    @Override
    public int size() {
        String[] fileNames = directory.list();
        if (fileNames == null) {
            throw new StorageException(directory.getName() + ":directory error", "");
        }
        return fileNames.length;
    }

    @Override
    public void clear() {
        File[] files = getFiles(directory);
        for (File f : files) {
            doDelete(f);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return serializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            serializer.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected List<Resume> doCopy() {
        File[] files = getFiles(directory);
        return Arrays.stream(files).map(this::doGet).collect(Collectors.toList());
    }

    private File[] getFiles(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException(directory.getName() + ":directory error", "");
        }
        return files;
    }
}
