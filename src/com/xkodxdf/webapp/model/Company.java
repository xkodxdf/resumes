package com.xkodxdf.webapp.model;

import com.xkodxdf.webapp.util.DateUtil;
import com.xkodxdf.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.xkodxdf.webapp.util.DateUtil.NOW;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Company EMPTY = new Company("", "", Period.EMPTY);

    private Link homePage;
    private List<Period> periods;

    public Company() {
    }

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
        Objects.requireNonNull(name);
        Objects.requireNonNull(website);
        Objects.requireNonNull(periods);
        this.homePage = new Link(name, website);
        this.periods = periods;
    }

    public Link getHomePage() {
        return homePage;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void addPeriod(Period period) {
        periods.add(period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(homePage, company.homePage) && Objects.equals(periods, company.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homePage, periods);
    }

    @Override
    public String toString() {
        return "Company{" +
                "homePage=" + homePage +
                ", periods=" + periods +
                '}';
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {

        private static final long serialVersionUID = 1L;

        public static final Period EMPTY = new Period("", "", LocalDate.MIN, LocalDate.MAX);

        private String title;
        private String description;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate endDate;

        public Period() {
        }

        public Period(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public Period(String title, String description, int startYear, Month startMonth) {
            this(title, description, DateUtil.of(startYear, startMonth), NOW);
        }

        public Period(String title, String description, int startYear, Month startMonth, int endYear, Month endMonth) {
            this(title, description, DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth));
        }

        public Period(String title, String description, LocalDate startDate, LocalDate endDate) {
            Objects.requireNonNull(title, "title must not be null");
            Objects.requireNonNull(description, "description must not be null");
            Objects.requireNonNull(startDate, "startDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null");
            this.title = title;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
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
            return "Period{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    '}';
        }
    }
}
