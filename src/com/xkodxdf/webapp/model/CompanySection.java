package com.xkodxdf.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {

    private List<Company> content;


    public CompanySection(SectionType type) {
        this(type, new ArrayList<>());
    }

    public CompanySection(SectionType type, List<Company> content) {
        super(type);
        Objects.requireNonNull(content);
        this.content = content;
    }


    public List<Company> getContent() {
        return content;
    }

    public void setContent(List<Company> content) {
        Objects.requireNonNull(content);
        this.content = content;
    }


    @Override
    public void printContent() {
        content.forEach(System.out::println);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySection that = (CompanySection) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }

    @Override
    public String toString() {
        return "PeriodSection{" +
                "content=" + content +
                '}';
    }
}
