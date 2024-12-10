package com.xkodxdf.webapp.web;

import com.xkodxdf.webapp.Config;
import com.xkodxdf.webapp.model.Resume;
import com.xkodxdf.webapp.storage.Storage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ResumeServlet extends HttpServlet {

    private final Storage sqlStorage = Config.get().getSqlStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
//        String name = request.getParameter("name");
//        response.getWriter().write(name == null ? "Hello Resumes!" : "<h1>Hello " + name + "!</h1>");
        Writer writer = response.getWriter();
        writer.write(
                "<table border='3'>" +
                        "<tr>" +
                        "<th>uuid</th>" +
                        "<th>full_name</th>" +
                        "</tr>");
        for (Resume r : sqlStorage.getAllSorted()) {
            writer.write(
                    "<tr>" +
                            "<td>" + r.getUuid() + "</td>" +
                            "<td>" + r.getFullName() + "</td>" +
                            "</tr>");
        }
        writer.write("</table>");
    }
}
