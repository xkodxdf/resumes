package com.xkodxdf.webapp.model;

import java.util.Objects;

public abstract class Section implements Comparable<Section> {

    private final SectionType type;


    public Section(SectionType type) {
        Objects.requireNonNull(type);
        this.type = type;
    }


    public SectionType getType() {
        return type;
    }


    public final void printSection() {
        System.out.println(type.getTitle());
        printContent();
        System.out.println();
    }


    protected abstract void printContent();


    @Override
    public int compareTo(Section s) {
        return type.ordinal() - s.type.ordinal();
    }
}
