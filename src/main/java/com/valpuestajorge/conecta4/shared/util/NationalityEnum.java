package com.valpuestajorge.conecta4.shared.util;

import lombok.Getter;

@Getter
public enum NationalityEnum {

    SPANISH("Español"),
    ENGLISH("English"),
    FRENCH("Français"),
    ITALIAN("Italiano"),
    GERMAN("Deutsch"),
    RUSSIAN("Русский");

    private final String language;

    NationalityEnum(String language) {
        this.language = language;
    }
}
