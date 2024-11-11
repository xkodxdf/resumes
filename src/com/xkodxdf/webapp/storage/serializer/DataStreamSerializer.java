package com.xkodxdf.webapp.storage.serializer;

import com.xkodxdf.webapp.model.*;
import com.xkodxdf.webapp.util.LocalDateAdapter;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            return readResume(dis);
        }
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            writeResume(r, dos);
        }
    }

    private Resume readResume(DataInputStream dis) throws IOException {
        String uuid = dis.readUTF();
        String fullName = dis.readUTF();
        int contactsSize = dis.readInt();
        int sectionsSize = dis.readInt();
        try {
            return new Resume(uuid, fullName,
                    readContacts(contactsSize, dis), readSections(sectionsSize, dis));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void writeResume(Resume r, DataOutputStream dos) throws IOException {
        dos.writeUTF(r.getUuid());
        dos.writeUTF(r.getFullName());
        try {
            writeContent(r, dos);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void writeContent(Resume r, DataOutputStream dos) throws Exception {
        dos.writeInt(r.getContacts().size());
        dos.writeInt(r.getSections().size());
        writeContacts(r.getContacts(), dos);
        writeSections(r.getSections(), dos);
    }

    private Map<ContactType, String> readContacts(int contactsSize, DataInputStream dis) throws IOException {
        return new HashMap<>() {{
            for (int i = 0; i < contactsSize; i++) {
                put(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
        }};
    }

    private void writeContacts(Map<ContactType, String> contacts, DataOutputStream dos) throws IOException {
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private Map<SectionType, Section> readSections(int sectionsSize, DataInputStream dis) throws Exception {
        SectionType sectionType;
        Map<SectionType, Section> ret = new HashMap<>();
        for (int i = 0; i < sectionsSize; i++) {
            sectionType = SectionType.valueOf(dis.readUTF());
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    ret.put(sectionType, new TextSection(dis.readUTF()));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    ret.put(sectionType, new ListSection(readListSectionContent(dis)));
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    ret.put(sectionType, new CompanySection(readCompanySectionContent(dis)));
                    break;
                default:
                    throw new IOException("invalid section type: " + sectionType.name());
            }
        }
        return ret;
    }

    private void writeSections(Map<SectionType, Section> sections, DataOutputStream dos) throws Exception {
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            SectionType sectionType = entry.getKey();
            dos.writeUTF(sectionType.name());
            Section section = entry.getValue();
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    writeTextSection((TextSection) section, dos);
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    writeListSection((ListSection) section, dos);
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    writeCompanySection((CompanySection) section, dos);
                    break;
                default:
                    throw new IOException("invalid section type: " + sectionType.name());
            }
        }
    }

    private void writeTextSection(TextSection section, DataOutputStream dos) throws IOException {
        dos.writeUTF(section.getContent());
    }

    private void writeListSection(ListSection section, DataOutputStream dos) throws IOException {
        dos.writeInt(section.getContent().size());
        for (String s : section.getContent()) {
            dos.writeUTF(s);
        }
    }

    private List<String> readListSectionContent(DataInputStream dis) throws IOException {
        int size = dis.readInt();
        return new ArrayList<>() {{
            for (int i = 0; i < size; i++) {
                add(dis.readUTF());
            }
        }};
    }

    private void writeCompanySection(CompanySection section, DataOutputStream dos) throws Exception {
        dos.writeInt(section.getContent().size());
        writeCompanies(section.getContent(), dos);

    }

    private static List<Company> readCompanySectionContent(DataInputStream dis) throws Exception {
        LocalDateAdapter adapter = new LocalDateAdapter();
        List<Company.Period> periods = new ArrayList<>();
        return new ArrayList<>() {{
            int companiesSize = dis.readInt();
            for (int i = 0; i < companiesSize; i++) {
                Link homePage = new Link(dis.readUTF(), dis.readUTF());
                int periodsSize = dis.readInt();
                for (int p = 0; p < periodsSize; p++) {
                    periods.add(new Company.Period(dis.readUTF(), dis.readUTF(),
                            adapter.unmarshal(dis.readUTF()), adapter.unmarshal(dis.readUTF())));
                }
                add(new Company(homePage.getName(), homePage.getUrl(), new ArrayList<>(periods)));
                periods.clear();
            }
        }};
    }

    private void writeCompanies(List<Company> companies, DataOutputStream dos) throws Exception {
        for (Company c : companies) {
            writeCompanyHomePage(c.getHomePage(), dos);
            writeCompanyPeriods(c.getPeriods(), dos);
        }
    }

    private void writeCompanyHomePage(Link link, DataOutputStream dos) throws IOException {
        dos.writeUTF(link.getName());
        dos.writeUTF(link.getUrl());
    }

    private void writeCompanyPeriods(List<Company.Period> periods, DataOutputStream dos) throws Exception {
        dos.writeInt(periods.size());
        for (Company.Period p : periods) {
            dos.writeUTF(p.getTitle());
            dos.writeUTF(p.getDescription());
            writePeriodDates(p.getStartDate(), p.getEndDate(), dos);
        }
    }

    private void writePeriodDates(LocalDate startDate, LocalDate endDate, DataOutputStream dos) throws Exception {
        LocalDateAdapter adapter = new LocalDateAdapter();
        dos.writeUTF(adapter.marshal(startDate));
        dos.writeUTF(adapter.marshal(endDate));
    }
}
