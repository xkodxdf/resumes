package com.xkodxdf.webapp;

import com.xkodxdf.webapp.model.*;
import com.xkodxdf.webapp.util.DateUtil;

import java.time.Month;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume testResume = new Resume("uuid1", "Name SecondName", generateContacts(), generateSections());
        System.out.println(testResume);
        System.out.println("_".repeat(80));
        printResume(testResume);
    }

    public static Resume getTestResume(String uuid, String fullName) {
        return new Resume(uuid, fullName, generateContacts(), generateSections());
    }

    private static void printResume(Resume resume) {
        System.out.println("\n" + resume.getFullName() + "\n");
        resume.getContacts().entrySet().forEach(System.out::println);
        System.out.println();
        resume.getSections().entrySet().forEach(System.out::println);
    }

    private static Map<ContactType, String> generateContacts() {
        return new EnumMap<>(ContactType.class) {{
            put(ContactType.SKYPE, (int) (Math.random() * 10_000) + "@example111");
            put(ContactType.EMAIL, (int) (Math.random() * 10_000) + "example@example.com");
            put(ContactType.LINKEDIN, (int) (Math.random() * 10_000) + "linked.in/example");
            put(ContactType.GITHUB, (int) (Math.random() * 10_000) + "github.com/example");
            put(ContactType.STACKOVERFLOW, (int) (Math.random() * 10_000) + "stackoverflow.com/example");
            put(ContactType.HOMEPAGE, (int) (Math.random() * 10_000) + "homepage.com");
        }};
    }

    private static Map<SectionType, Section> generateSections() {
        TextSection objective = new TextSection((int) (Math.random() * 10_000) +
                "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям..."
        );

        TextSection personal = new TextSection((int) (Math.random() * 10_000) +
                "Аналитический склад ума, сильная логика, креативность, инициативность." +
                "Пурист кода и архитектуры..."
        );

        ListSection achievements = new ListSection(
                Arrays.asList((int) (Math.random() * 10_000) +
                                "Организация команды и успешная реализация Java проектов для сторонних заказчиков...",
                        "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\"...",
                        "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike..."
                )
        );

        ListSection qualification = new ListSection(
                Arrays.asList((int) (Math.random() * 10_000) +
                                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2...",
                        "Version control: Subversion, Git, Mercury, ClearCase, Perforce...",
                        "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite," +
                                "MS SQL, HSQLDB..."
                )
        );

        CompanySection experience = new CompanySection(
                Arrays.asList(
                        new Company(
                                "Alcatel", "alcatel.com",
                                new Company.Period(
                                        "Инженер по аппаратному и программному тестированию",
                                        "Тестирование, отладка, внедрение ПО цифровой телефонной станции " +
                                                "Alcatel 1000 S12 (CHILL, ASM).",
                                        DateUtil.of(1997, Month.AUGUST), DateUtil.of(2004, Month.OCTOBER))),
                        new Company(
                                "Siemens AG", "siemens.com",
                                new Company.Period(
                                        "Разработчик ПО",
                                        "Разработка информационной модели, проектирование интерфейсов," +
                                                "реализация и отладка ПО на мобильной IN платформе" +
                                                "Siemens @vantage (Java, Unix).",
                                        DateUtil.of(2005, Month.SEPTEMBER), DateUtil.of(2006, Month.JULY)
                                ))
                )
        );

        CompanySection education = new CompanySection(
                Arrays.asList(
                        new Company(
                                "Заочная физико-техническая школа при МФТИ",
                                new Company.Period(
                                        "", "Закончил с отличием", DateUtil.of(1988, Month.OCTOBER),
                                        DateUtil.of(1990, Month.DECEMBER)
                                )),

                        new Company(
                                "Alcatel", "alcatel.com",
                                new Company.Period(
                                        "", "6 месяцев обучения цифровым телефонным сетям (Москва)",
                                        DateUtil.of(1992, Month.AUGUST), DateUtil.of(1996, Month.JUNE)
                                ))

                )
        );

        return new EnumMap<>(SectionType.class) {{
            put(SectionType.OBJECTIVE, objective);
            put(SectionType.PERSONAL, personal);
            put(SectionType.ACHIEVEMENT, achievements);
            put(SectionType.QUALIFICATIONS, qualification);
//            put(SectionType.EXPERIENCE, experience);
//            put(SectionType.EDUCATION, education);
        }};
    }
}
