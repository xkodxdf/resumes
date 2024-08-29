import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[4];

    void clear() {
        for (int i = 0; i < storage.length; i++) {
            if (!Objects.isNull(storage[i])) {
                storage[i] = null;
            } else {
                break;
            }
        }
    }

    void save(Resume r) {
        for (int i = 0; i < storage.length; i++) {
            if (Objects.isNull(storage[i])) {
                storage[i] = r;
                break;
            }
        }
    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            if (Objects.isNull(resume)) {
                break;
            } else if (uuid.equals(resume.uuid)) {
                return resume;
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < storage.length; i++) {
            if (Objects.isNull(storage[i])) {
                break;
            } else if (uuid.equals(storage[i].uuid) && i == storage.length - 1) {
                storage[i] = null;
                break;
            } else if (uuid.equals(storage[i].uuid)
                    && i < storage.length - 1
                    && Objects.isNull(storage[i + 1])) {
                storage[i] = null;
                break;
            } else if (uuid.equals(storage[i].uuid)
                    && i < storage.length - 1
                    && !Objects.isNull(storage[i + 1])) {
                while (!Objects.isNull(storage[i])) {
                    if (i == storage.length - 1) {
                        storage[i] = null;
                        return;
                    }
                    storage[i] = storage[++i];
                }
                break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return new Resume[0];
    }

    int size() {
        return 0;
    }
}
