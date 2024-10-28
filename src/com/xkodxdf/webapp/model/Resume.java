package com.xkodxdf.webapp.model;

import java.util.*;


public class Resume implements Comparable<Resume> {

    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts;
    private final Map<SectionType, Section> sections;


    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this(uuid, fullName, new EnumMap<>(ContactType.class));
    }

    public Resume(String uuid, String fullName, Map<ContactType, String> contacts) {
        this(uuid, fullName, contacts, new EnumMap<>(SectionType.class));
    }

    public Resume(String uuid, String fullName, Map<ContactType, String> contacts, Map<SectionType, Section> sections) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        Objects.requireNonNull(contacts, "contacts must not be null");
        Objects.requireNonNull(sections, "sections must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
        this.contacts = new EnumMap<>(contacts);
        this.sections = new EnumMap<>(sections);
    }


    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }


    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public void addContact(ContactType type, String contact) {
        contacts.put(type, contact);
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public void addSection(SectionType type, Section section) {
        sections.put(type, section);
    }


    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName)
                && Objects.equals(contacts, resume.contacts) && Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", contacts=" + contacts +
                ", sections=" + sections +
                '}';
    }
}