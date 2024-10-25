package com.xkodxdf.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section {

    private List<String> content;


    public ListSection(SectionType type) {
        this(type, new ArrayList<>());
    }

    public ListSection(SectionType type, List<String> content) {
        super(type);
        Objects.requireNonNull(content);
        this.content = content;
    }


    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        Objects.requireNonNull(content);
        this.content = content;
    }


    @Override
    public void printContent() {
        content.forEach(e -> System.out.println("* " + e));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }

    @Override
    public String toString() {
        return "ListSection{" +
                "content=" + content +
                '}';
    }
}
