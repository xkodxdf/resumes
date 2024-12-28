package com.xkodxdf.webapp.web;

import com.xkodxdf.webapp.Config;
import com.xkodxdf.webapp.model.*;
import com.xkodxdf.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {

    private final Storage storage = Config.get().getSqlStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume;
        boolean isNew = false;
        if (uuid == null || uuid.isEmpty()) {
            resume = new Resume(fullName);
            isNew = true;
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String companyName = request.getParameter(type.name());
            String[] companyNames = request.getParameterValues(type.name());
            boolean isCompanyNameEmpty = (companyName == null || companyName.trim().isEmpty());
            boolean isCompanyNamesEmpty = (companyNames.length <= 2 && companyNames[0].isEmpty());
            if (isCompanyNameEmpty && isCompanyNamesEmpty) {
                resume.getSections().remove(type);
            } else if (companyName != null) {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(type, new TextSection(companyName));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(type, new ListSection(Arrays.stream(companyName.split("\n"))
                                .filter(s -> !s.trim().isEmpty()).collect(Collectors.toList())));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Company> companies = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < companyNames.length; i++) {
                            String name = companyNames[i];
                            if ((name != null) && !(name.trim().isEmpty())) {
                                String commonMark = type.name() + i;
                                List<Company.Period> periods = new ArrayList<>();
                                String url = urls[i];
                                String[] startDates = request.getParameterValues(commonMark + "start_date");
                                String[] endDates = request.getParameterValues(commonMark + "end_date");
                                String[] titles = request.getParameterValues(commonMark + "title");
                                String[] descriptions = request.getParameterValues(commonMark + "description");
                                for (int k = 0; k < titles.length; k++) {
                                    if (titles[k] != null && !titles[k].trim().isEmpty()) {
                                        Company.Period period = new Company.Period(titles[k], descriptions[k]);
                                        if (!startDates[k].isEmpty()) {
                                            period.setStartDate(LocalDate.parse(startDates[k]));
                                        }
                                        if (!(startDates[k].isEmpty()) && !(endDates[k].isEmpty())) {
                                            period.setEndDate(LocalDate.parse(endDates[k]));
                                        }
                                        periods.add(period);
                                    }
                                }
                                companies.add(new Company(name, url, periods));
                            }
                        }
                        resume.addSection(type, new CompanySection(companies));
                }
            }
        }
        if (isNew) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "add":
                resume = Resume.EMPTY;
                break;
            case "view":
                resume = storage.get(uuid);
                break;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "edit":
                resume = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    Section resumeSection = resume.getSection(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (resumeSection == null) {
                                resume.addSection(type, TextSection.EMPTY);
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (resumeSection == null) {
                                resume.addSection(type, ListSection.EMPTY);
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            if (resumeSection == null) {
                                resume.addSection(type, CompanySection.EMPTY);
                            } else {
                                CompanySection companySection = (CompanySection) resumeSection;
                                for (Company company : companySection.getContent()) {
                                    if (company.getPeriods().isEmpty()) {
                                        company.addPeriod(Company.Period.EMPTY);
                                    }
                                }
                                companySection.getContent().add(Company.EMPTY);
                            }
                            break;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}
