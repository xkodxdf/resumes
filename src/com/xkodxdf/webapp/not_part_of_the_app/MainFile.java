package com.xkodxdf.webapp.not_part_of_the_app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class MainFile {

    public static void main(String[] args) {
        File baseDir = new File(System.getProperty("user.dir"));
        printProjectStructure(baseDir, "");
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

    private static void printProjectStructure(File root, String indent) {
        File[] files = root.listFiles();
        if (Objects.nonNull(files)) {
            for (File file : files) {
                System.out.println(file.isDirectory() ?
                        indent + "DIRECTORY:" + file.getName().toUpperCase()
                        : indent + "./" + file.getParentFile().getName() + "/" + file.getName());
                printProjectStructure(file, indent + "| ");
            }
        }
    }
}
