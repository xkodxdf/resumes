package com.xkodxdf.webapp.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section {

    private final List<String> content;


    public ListSection(List<String> content) {
        Objects.requireNonNull(content);
        this.content = content;
    }


    public List<String> getContent() {
        return content;
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
