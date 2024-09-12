package com.xkodxdf.webapp.storage;

import com.xkodxdf.webapp.model.Resume;

import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int resumesQuantity = 0;
    private final int STORAGE_LIMIT = 10_000;
    private final Resume[] storage = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(storage, 0, resumesQuantity, null);
        resumesQuantity = 0;
    }

    public void save(Resume r) {
        if (!Objects.isNull(r) && !Objects.isNull(r.getUuid())) {
            if (getIndex(r.getUuid()) != -1) {
                System.out.println("Резюме с идентификатором [" + r.getUuid() + "] уже присутствует в хранилище.");
            } else if (resumesQuantity == storage.length) {
                System.out.println("Хранилище переполнено");
            } else {
                storage[resumesQuantity] = r;
                resumesQuantity++;
            }
        }
    }

    public Resume get(String uuid) {
        int resumePosition = getIndex(uuid);
        if (resumePosition != -1) {
            return storage[resumePosition];
        }
        System.out.println("По идентификатору [" + uuid + "] ничего не найдено.");
        return null;
    }

    public void update(Resume r) {
        if (!Objects.isNull(r) && !Objects.isNull(r.getUuid())) {
            int resumePosition = getIndex(r.getUuid());
            if (resumePosition == -1) {
                System.out.println("Резюме c идентификатором [" + r.getUuid() + "] не найдено.");
            } else {
                storage[resumePosition] = r;
            }
        }
    }

    public void delete(String uuid) {
        int resumePosition = getIndex(uuid);
        if (resumePosition != -1) {
            storage[resumePosition] = storage[resumesQuantity - 1];
            storage[resumesQuantity - 1] = null;
            resumesQuantity--;
        } else {
            System.out.println("По идентификатору [" + uuid + "] ничего не найдено.");
        }
    }

    protected int getIndex(String uuid) {
        if (!Objects.isNull(uuid)) {
            for (int i = 0; i < resumesQuantity; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, resumesQuantity);
    }

    public int size() {
        return resumesQuantity;
    }
}
