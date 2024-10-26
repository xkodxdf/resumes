package com.xkodxdf.webapp.model;

import java.util.Objects;

public class Contact {

    private String content;
    private String website;


    public Contact(String content) {
        this(content, "");
    }

    public Contact(String content, String website) {
        Objects.requireNonNull(content, "content must not be null");
        Objects.requireNonNull(website, "website must not be null");
        this.content = content;
        this.website = website;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        Objects.requireNonNull(content);
        this.content = content;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    @Override
    public boolean equals(Object index) {
        if (this == index) return true;
        if (index == null || getClass() != index.getClass()) return false;

        Contact contact = (Contact) index;
        return Objects.equals(content, contact.content) && Objects.equals(website, contact.website);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(content);
        result = 31 * result + Objects.hashCode(website);
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "content='" + content + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}

