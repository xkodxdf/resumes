package com.xkodxdf.webapp.not_part_of_the_app;

import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.AbstractStorage;
import com.xkodxdf.webapp.storage.ArrayStorage;


public class MainTestArrayStorage {
    static final AbstractStorage<Integer> ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume("uuid1", "dummy1");
        Resume r2 = new Resume("uuid2", "dummy2");
        Resume r3 = new Resume("uuid3", "dummy3");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
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
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}