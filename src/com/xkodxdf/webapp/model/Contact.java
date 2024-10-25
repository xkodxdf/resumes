package com.xkodxdf.webapp.model;

import java.util.Objects;

public class Contact implements Comparable<Contact> {

    private final ContactType type;
    private String content;
    private String website;


    public Contact(ContactType type) {
        this(type, "", "");
    }

    public Contact(ContactType type, String content) {
        this(type, content, "");
    }

    public Contact(ContactType type, String content, String website) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(content, "content must not be null");
        Objects.requireNonNull(website, "website must not be null");
        this.type = type;
        this.content = content;
        this.website = website;
    }


    public ContactType getType() {
        return type;
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


    public void printContact() {
        System.out.println(type.getType() + ": " + content);
    }


    @Override
    public int compareTo(Contact c) {
        return type.ordinal() - c.type.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return type == contact.type && Objects.equals(content, contact.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}

