import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[4];

    void clear() {
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
        return null;
    }

    void delete(String uuid) {
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
