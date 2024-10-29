package com.xkodxdf.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class MainFile {

    public static void main(String[] args) {
        File baseDir = new File(System.getProperty("user.dir"));
        printProjectStructure(baseDir);
        System.out.println("#".repeat(80));


        String filePath = ".\\.gitignore";
        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/com/xkodxdf/webapp");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printProjectStructure(File root) {
        File[] files = root.listFiles();
        if (Objects.nonNull(files)) {
            for (File file : files) {
                String[] path = file.getParent().split("\\\\");
                System.out.println(file.isDirectory() ?
                        "\nDIRECTORY:" + file.getName().toUpperCase()
                        : "../" + path[path.length - 1] + "/" + file.getName());
                printProjectStructure(file);
            }
        }
    }
}
