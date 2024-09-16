package com.xkodxdf.webapp;

import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.SortedArrayStorage;
import com.xkodxdf.webapp.storage.Storage;

/**
 * Test for your com.xkodxdf.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    static final Storage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        System.out.println(ARRAY_STORAGE.getClass().getName());
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r4 = new Resume();
        r4.setUuid("uuid3");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        System.out.print("r3 == ARRAY_STORAGE.get(\"uuid3\"): ");
        System.out.println(r3 == ARRAY_STORAGE.get("uuid3"));
        ARRAY_STORAGE.update(r4);
        System.out.println("After update r3 to r4:");
        System.out.print("r3 == ARRAY_STORAGE.get(\"uuid3\"): ");
        System.out.println(r3 == ARRAY_STORAGE.get("uuid3"));


        System.out.println("\nGet r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}