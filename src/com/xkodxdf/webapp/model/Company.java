package com.xkodxdf.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Company {

    private final String name;
    private final String website;
    private final List<Period> periods;


    public Company(String name, Period... periods) {
        this(name, "", Arrays.asList(periods));
    }

    public Company(String name, List<Period> periods) {
        this(name, "", periods);
    }

    public Company(String name, String website, Period... periods) {
        this(name, website, Arrays.asList(periods));

    }

    public Company(String name, String website, List<Period> periods) {
        this.name = name;
        this.website = website;
        this.periods = periods;
    }


    @Override
    public String toString() {
        return "# Имя организации: " + name +
                "\nсайт: " + website + "\n" + periods;
    }


    public static class Period {

        private String title;
        private String description;
        private String startDate;
        private String endDate;


        public Period(String title, String description, String startDate, String endDate) {
            this.title = title;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            Objects.requireNonNull(title);
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            Objects.requireNonNull(description);
            this.description = description;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            Objects.requireNonNull(startDate);
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            Objects.requireNonNull(endDate);
            this.endDate = endDate;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return Objects.equals(title, period.title) && Objects.equals(description, period.description)
                    && Objects.equals(startDate, period.startDate) && Objects.equals(endDate, period.endDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description, startDate, endDate);
        }

        @Override
        public String toString() {
            return "Должность: " + title + "\nОписание: " + description +
                    "\nДата начала: " + startDate + " - " + "Дата окончания: " + endDate;
        }
    }
}
