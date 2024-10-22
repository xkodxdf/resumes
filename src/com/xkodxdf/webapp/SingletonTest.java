package com.xkodxdf.webapp;

import com.xkodxdf.webapp.model.SectionType;

public class SingletonTest {

    private static SingletonTest instance;

    private SingletonTest() {
    }

    public static SingletonTest getInstance() {
        if (instance == null) {
            instance = new SingletonTest();
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(SingletonTest.getInstance().toString());
        Singleton instance = Singleton.valueOf("INSTANCE");
        System.out.println(instance.ordinal());

        for (SectionType type : SectionType.values()) {
            System.out.println(type.getTitle());
        }
    }

    public enum Singleton {
        INSTANCE
    }
}
