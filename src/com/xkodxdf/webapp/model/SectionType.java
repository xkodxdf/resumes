package com.xkodxdf.webapp.model;

public enum SectionType {

    OBJECTIVE("Позиция"),
    PERSONAL("Личные качества"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private final String type;

    SectionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
