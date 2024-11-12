package com.xkodxdf.webapp.storage.serializer;

import com.xkodxdf.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {

    private interface Writer<T> {
        void write(T element) throws IOException;
    }

    private interface Reader<T> {
        T read() throws IOException;
    }

    private interface EntryReader<K, V> {
        Map.Entry<K, V> read() throws IOException;
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String UUID = dis.readUTF();
            String fullName = dis.readUTF();
            Map<ContactType, String> contacts = readMap(dis.readInt(),
                    () -> Map.entry(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            Map<SectionType, Section> sections = readMap(dis.readInt(), () -> readSection(dis));
            return new Resume(UUID, fullName, contacts, sections);
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeContacts(r.getContacts(), dos);
            writeSections(r.getSections(), dos);
        }
    }

    private Map.Entry<SectionType, Section> readSection(DataInputStream dis) throws IOException {
        SectionType sectionType = SectionType.valueOf(dis.readUTF());
        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                return Map.entry(sectionType, new TextSection(dis.readUTF()));
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return Map.entry(sectionType, new ListSection(readList(dis.readInt(), dis::readUTF)));
            case EXPERIENCE:
            case EDUCATION:
                return Map.entry(sectionType, readCompanySection(dis));
            default:
                throw new IOException("invalid section type: " + sectionType.name());
        }
    }

    private CompanySection readCompanySection(DataInputStream dis) throws IOException {
        return new CompanySection(readList(dis.readInt(),
                () -> new Company(dis.readUTF(), dis.readUTF(), readList(dis.readInt(),
                        () -> new Company.Period(dis.readUTF(), dis.readUTF(),
                                LocalDate.parse(dis.readUTF()),
                                LocalDate.parse(dis.readUTF()))))));
    }

    private void writeContacts(Map<ContactType, String> contacts, DataOutputStream dos) throws IOException {
        dos.writeInt(contacts.size());
        writeCollection(contacts.entrySet(), entry -> {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        });
    }

    private void writeSections(Map<SectionType, Section> sections, DataOutputStream dos) throws IOException {
        dos.writeInt(sections.size());
        writeCollection(sections.entrySet(), entry -> writeSection(entry, dos));
    }

    private void writeSection(Map.Entry<SectionType, Section> sectionEntry, DataOutputStream dos) throws IOException {
        SectionType sectionType = sectionEntry.getKey();
        Section section = sectionEntry.getValue();
        dos.writeUTF(sectionType.name());
        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                dos.writeUTF(((TextSection) section).getContent());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                writeListSection(dos, (ListSection) section);
                break;
            case EXPERIENCE:
            case EDUCATION:
                writeCompanySection(dos, (CompanySection) section);
                break;
            default:
                throw new IOException("Invalid section type: " + sectionType.name());
        }
    }

    private void writeListSection(DataOutputStream dos, ListSection section) throws IOException {
        dos.writeInt(section.getContent().size());
        writeCollection(section.getContent(), dos::writeUTF);
    }

    private void writeCompanySection(DataOutputStream dos, CompanySection section) throws IOException {
        dos.writeInt(section.getContent().size());
        writeCollection(section.getContent(), company -> {
            dos.writeUTF(company.getHomePage().getName());
            dos.writeUTF(company.getHomePage().getUrl());
            writeCollection(company.getPeriods(), period -> {
                dos.writeInt(company.getPeriods().size());
                dos.writeUTF(period.getTitle());
                dos.writeUTF(period.getDescription());
                dos.writeUTF(period.getStartDate().toString());
                dos.writeUTF(period.getEndDate().toString());
            });
        });
    }

    private <T> void writeCollection(Collection<T> collection, Writer<T> writer)
            throws IOException {
        for (T element : collection) {
            writer.write(element);
        }
    }

    private <T> List<T> readList(int size, Reader<T> reader) throws IOException {
        return new ArrayList<>() {{
            for (int i = 0; i < size; i++) {
                add(reader.read());
            }
        }};
    }

    private <K, V> Map<K, V> readMap(int size, EntryReader<K, V> entryReader) throws IOException {
        return new HashMap<>() {{
            for (int i = 0; i < size; i++) {
                Map.Entry<K, V> entry = entryReader.read();
                put(entry.getKey(), entry.getValue());
            }
        }};
    }
}
