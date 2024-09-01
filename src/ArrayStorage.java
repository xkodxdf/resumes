import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int resumesQuantity = 0;
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < resumesQuantity; i++) {
            storage[i] = null;
        }
        resumesQuantity = 0;
    }

    void save(Resume r) {
        if (!Objects.isNull(r)
                && !Objects.isNull(r.uuid)
                && resumesQuantity < storage.length) {
            storage[resumesQuantity] = r;
            resumesQuantity++;
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < resumesQuantity; i++) {
            if (uuid.equals(storage[i].uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < resumesQuantity; i++) {
            if (uuid.equals(storage[i].uuid)) {
                while (i < resumesQuantity) {
                    if (i == resumesQuantity - 1) {
                        storage[i] = null;
                        resumesQuantity--;
                        return;
                    }
                    storage[i] = storage[i + 1];
                    i++;
                }
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, resumesQuantity);
    }

    int size() {
        return resumesQuantity;
    }
}
