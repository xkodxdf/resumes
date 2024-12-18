package com.xkodxdf.webapp.web;

import com.xkodxdf.webapp.Config;
import com.xkodxdf.webapp.model.ContactType;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {

    private final Storage storage = Config.get().getSqlStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        if (fullName.trim().isEmpty()) {
            request.setAttribute("nameErrMsg", "Поле ФИО не должно быть пустым или содержать только пробелы");
            request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
            return;
        }
            Resume r;
            boolean isNew = false;
            if (uuid == null || uuid.isEmpty()) {
                r = new Resume(UUID.randomUUID().toString());
                isNew = true;
            } else {
                r = storage.get(uuid);
            }
            r.setFullName(fullName);
            for (ContactType type : ContactType.values()) {
                String value = request.getParameter(type.name());
                if (value != null && !value.trim().isEmpty()) {
                    r.addContact(type, value);
                } else {
                    r.getContacts().remove(type);
                }
            }
            if (isNew) {
                storage.save(r);
            } else {
                storage.update(r);
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
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "save":
                r = new Resume();
                break;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}
