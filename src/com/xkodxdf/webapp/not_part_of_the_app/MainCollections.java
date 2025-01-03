package com.xkodxdf.webapp.not_part_of_the_app;

import com.xkodxdf.webapp.model.Resume;

import java.util.*;

public class MainCollections {
    private static final String UUID_1 = "uuid1";
    private static final String NAME_1 = "dummy1";
    private static final Resume RESUME_1 = new Resume(UUID_1, NAME_1);

    private static final String UUID_2 = "uuid2";
    private static final String NAME_2 = "dummy2";
    private static final Resume RESUME_2 = new Resume(UUID_2, NAME_2);

    private static final String UUID_3 = "uuid3";
    private static final String NAME_3 = "dummy3";
    private static final Resume RESUME_3 = new Resume(UUID_3, NAME_3);

    private static final String UUID_4 = "uuid4";
    private static final String NAME_4 = "dummy4";
    private static final Resume RESUME_4 = new Resume(UUID_4, NAME_4);

    public static void main(String[] args) {
        Collection<Resume> collection = new ArrayList<>();
        collection.add(RESUME_1);
        collection.add(RESUME_2);
        collection.add(RESUME_3);

        for (Resume r : collection) {
            System.out.println(r);
            if (Objects.equals(r.getUuid(), UUID_1)) {
//                collection.remove(r);
            }
        }

        Iterator<Resume> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resume r = iterator.next();
            System.out.println(r);
            if (Objects.equals(r.getUuid(), UUID_1)) {
                iterator.remove();
            }
        }
        System.out.println(collection.toString());


        Map<String, Resume> map = new HashMap<>();
        map.put(UUID_1, RESUME_1);
        map.put(UUID_2, RESUME_2);
        map.put(UUID_3, RESUME_3);

        // Bad!
        for (String uuid : map.keySet()) {
            System.out.println(map.get(uuid));
        }

        for (Map.Entry<String, Resume> entry : map.entrySet()) {
            System.out.println(entry.getValue());
        }
        List<Resume> resumes = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);
        resumes.remove(1);
        System.out.println(resumes);
    }
}
