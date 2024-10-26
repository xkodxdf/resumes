package com.xkodxdf.webapp.model;

import java.util.*;


public class Resume implements Comparable<Resume> {

    private final String uuid;
    private final String fullName;
    private Map<ContactType, Contact> contacts;
    private Map<SectionType, Section> sections;


    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this(uuid, fullName, new EnumMap<>(ContactType.class));
    }

    public Resume(String uuid, String fullName, Map<ContactType, Contact> contacts) {
        this(uuid, fullName, contacts, new EnumMap<>(SectionType.class));
    }

    public Resume(String uuid, String fullName, Map<ContactType, Contact> contacts, Map<SectionType, Section> sections) {
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

    public Map<ContactType, Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Map<ContactType, Contact> contacts) {
        Objects.requireNonNull(contacts);
        this.contacts = new EnumMap<>(contacts);
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public void setSections(Map<SectionType, Section> sections) {
        Objects.requireNonNull(sections);
        this.sections = new EnumMap<>(sections);
    }


    public Contact getContact(ContactType type) {
        return contacts.get(type);
    }

    public void setContact(Contact contact) {
        Objects.requireNonNull(contact);
        contacts.put(contact.getType(), contact);
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public void setSection(Section section) {
        Objects.requireNonNull(section);
        sections.put(section.getType(), section);
    }

    public void printResume() {
        System.out.println("\n" + fullName + "\n");
        contacts.values().forEach(Contact::printContact);
        System.out.println();
        sections.values().forEach(Section::printSection);
    }


    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }

    @Override
    public boolean equals(Object index) {
        if (this == index) return true;
        if (index == null || getClass() != index.getClass()) return false;

        Resume resume = (Resume) index;
        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, sections);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}