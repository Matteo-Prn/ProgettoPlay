package com.play.model.enums;

/**
 * Questa classe rappresenta i diversi argomenti degli esercizi
 */
public enum Topic {
    FUNDAMENTALS("fundamentals"),
    METHODS("methods"),
    ADVANCED_CLASSES("advanced_classes"),
    EXCEPTIONS("exceptions"),
    IO("io");

    private final String textUIKey;

    Topic(String textUIKey) {
        this.textUIKey = textUIKey;
    }

    public String getTextUIKeyy() {
        return textUIKey;
    }

}
