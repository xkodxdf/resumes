package com.xkodxdf.webapp.model;

import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {

    private List<Company> content;


    public CompanySection(List<Company> content) {
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
        return "CompanySection{" +
                "content=" + content +
                '}';
    }
}
