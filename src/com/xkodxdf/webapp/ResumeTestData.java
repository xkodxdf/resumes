package com.xkodxdf.webapp;

import com.xkodxdf.webapp.model.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume testResume = new Resume("Name SecondName");
        testResume.setContacts(generateContacts());
        testResume.setSections(generateSections());
        testResume.printResume();
    }


    private static Map<ContactType, Contact> generateContacts() {
        Contact phone = new Contact(ContactType.PHONE, "8-999-999-99-99");
        Contact skype = new Contact(ContactType.SKYPE, "@example111");
        Contact email = new Contact(ContactType.EMAIL, "example@example.com");
        Contact linkedIn = new Contact(ContactType.LINKEDIN, "linked.in/example");
        Contact gitHub = new Contact(ContactType.GITHUB, "github.com/example");
        Contact stackOverFlow = new Contact(ContactType.STACKOVERFLOW, "stackoverflow.com/example");
        Contact homePage = new Contact(ContactType.HOMEPAGE, "homepage.com");

        return new EnumMap<>(ContactType.class) {
            {
                put(phone.getType(), phone);
                put(skype.getType(), skype);
                put(email.getType(), email);
                put(linkedIn.getType(), linkedIn);
                put(gitHub.getType(), gitHub);
                put(stackOverFlow.getType(), stackOverFlow);
                put(homePage.getType(), homePage);
            }
        };
    }

    private static Map<SectionType, Section> generateSections() {
        TextSection objective = new TextSection(
                SectionType.OBJECTIVE,
                "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям..."
        );

        TextSection personal = new TextSection(
                SectionType.PERSONAL,
                "Аналитический склад ума, сильная логика, креативность, инициативность." +
                        "Пурист кода и архитектуры..."
        );

        ListSection achievements = new ListSection(
                SectionType.ACHIEVEMENT,
                Arrays.asList(
                        "Организация команды и успешная реализация Java проектов для сторонних заказчиков...",
                        "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\"...",
                        "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike..."
                )
        );

        ListSection qualification = new ListSection(
                SectionType.QUALIFICATIONS,
                Arrays.asList(
                        "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2...",
                        "Version control: Subversion, Git, Mercury, ClearCase, Perforce...",
                        "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite," +
                                "MS SQL, HSQLDB..."
                )
        );

        CompanySection experience = new CompanySection(
                SectionType.EXPERIENCE,
                Arrays.asList(
                        new Company(
                                "Alcatel",
                                new Company.Period(
                                        "Инженер по аппаратному и программному тестированию",
                                        "Тестирование, отладка, внедрение ПО цифровой телефонной станции " +
                                                "Alcatel 1000 S12 (CHILL, ASM).",
                                        "09/1997", "01/2005")),
                        new Company(
                                "Siemens AG",
                                new Company.Period(
                                        "Разработчик ПО",
                                        "Разработка информационной модели, проектирование интерфейсов," +
                                                "реализация и отладка ПО на мобильной IN платформе" +
                                                "Siemens @vantage (Java, Unix).",
                                        "01/2005", "02/2007"
                                ))
                )
        );

        CompanySection education = new CompanySection(
                SectionType.EDUCATION,
                Arrays.asList(
                        new Company(
                                "Заочная физико-техническая школа при МФТИ",
                                new Company.Period(
                                        "", "Закончил с отличием", "09/1984", "06/1987"
                                )),

                        new Company(
                                "Alcatel",
                                new Company.Period(
                                        "", "6 месяцев обучения цифровым телефонным сетям (Москва)",
                                        "09/1997", "03/1998"
                                ))

                )
        );

        return new EnumMap<>(SectionType.class) {
            {
                put(objective.getType(), objective);
                put(personal.getType(), personal);
                put(achievements.getType(), achievements);
                put(qualification.getType(), qualification);
                put(experience.getType(), experience);
                put(education.getType(), education);

            }
        };
    }
}
