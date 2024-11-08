package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.exception.StorageException;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.serializer.StreamSerializer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {

    private final Path directory;
    private StreamSerializer serializer;

    protected PathStorage(String dir, StreamSerializer serializer) {
        this.directory = Path.of(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("is not directory");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException("is not readable/writable");
        }
        Objects.requireNonNull(serializer, "serializer most not be null");
        this.serializer = serializer;
    }

    public void setSerializer(StreamSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public int size() {
        return (int) getFileStream().count();
    }

    @Override
    public void clear() {
        getFileStream().forEach(this::doDelete);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("IO error", e);
        }
        doUpdate(r, path);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serializer.doRead(new BufferedInputStream(new FileInputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", getFileName(path), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            serializer.doWrite(r, new BufferedOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", getFileName(path), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("File delete error", getFileName(path), e);
        }
    }

    @Override
    protected List<Resume> doCopy() {
        return getFileStream().map(this::doGet).collect(Collectors.toList());
    }

    private Stream<Path> getFileStream() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory error", e);
        }
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }
}
