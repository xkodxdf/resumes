package com.xkodxdf.webapp.model;

public enum ContactType {

    PHONE("Тел.") {
        @Override
        protected String toHtml0(String value) {
            return getTitle() + ": " + toLink("tel:" + value, value);
        }
    },
    MOBILE("Мобильный") {
        @Override
        protected String toHtml0(String value) {
            return getTitle() + ": " + toLink("tel:" + value, value);
        }
    },
    HOME_PHONE("Домашний тел.") {
        @Override
        protected String toHtml0(String value) {
            return getTitle() + ": " + toLink("tel:" + value, value);
        }
    },
    SKYPE("Skype") {
        @Override
        protected String toHtml0(String value) {
            return toLink("skype:" + value, value);
        }
    },
    EMAIL("Почта") {
        @Override
        protected String toHtml0(String value) {
            return toLink("mailto:" + value, value);
        }
    },
    LINKEDIN("LinkedIn") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    },
    GITHUB("GitHub") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    },
    STACKOVERFLOW("StackOverFlow") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    },
    HOMEPAGE("Домашняя страница") {
        @Override
        protected String toHtml0(String value) {
            return toLink(value);
        }
    };

    private final String title;

    ContactType(String type) {
        this.title = type;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    public String toLink(String href) {
        return toLink(href, title);
    }

    public String toLink(String href, String title) {
        return "<a href='" + href + "' id='" + this.name() + "'>" + title + "</a>";
    }
}
