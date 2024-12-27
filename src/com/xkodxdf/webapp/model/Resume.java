package com.xkodxdf.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final Resume EMPTY = new Resume();

    static {
        EMPTY.addSection(SectionType.OBJECTIVE, new TextSection(""));
        EMPTY.addSection(SectionType.PERSONAL, new TextSection(""));
        EMPTY.addSection(SectionType.ACHIEVEMENT, new ListSection(new ArrayList<>()));
        EMPTY.addSection(SectionType.QUALIFICATIONS, new ListSection(new ArrayList<>()));
        EMPTY.addSection(SectionType.EXPERIENCE, new CompanySection(List.of(new Company("", "",
                new Company.Period("", "", LocalDate.MIN, LocalDate.MAX)))));
        EMPTY.addSection(SectionType.EDUCATION, new CompanySection(List.of(new Company("", "",
                new Company.Period("", "", LocalDate.MIN, LocalDate.MAX)))));
    }

    private final String uuid;
    private String fullName;
    private final Map<ContactType, String> contacts;
    private final Map<SectionType, Section> sections;

    public Resume() {
        this("", "", new HashMap<>(), new HashMap<>());
    }

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
        this.contacts = contacts.isEmpty() ? new EnumMap<>(ContactType.class) : new EnumMap<>(contacts);
        this.sections = sections.isEmpty() ? new EnumMap<>(SectionType.class) : new EnumMap<>(sections);
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        int nameCompareResult = fullName.compareTo(o.fullName);
        return nameCompareResult != 0 ?
                nameCompareResult
                : uuid.compareTo(o.getUuid());
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