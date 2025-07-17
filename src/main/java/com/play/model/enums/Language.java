package com.play.model.enums;

import java.util.Locale;

/**
 * Rappresenta le lingue supportate dall'applicazione.
 */
public enum Language {
    IT("language.it", Locale.ITALIAN),
    EN("language.en", Locale.ENGLISH);

    private final String textUIKey;
    private final Locale locale;

    Language(String textUIKey, Locale locale) {
        this.textUIKey = textUIKey;
        this.locale = locale;
    }


    /**
     * @return la chiave per il testo dell'interfaccia utente.
     */
    public String getTextUIKey() {
        return textUIKey;
    }

    /**
     * @return il {@link Locale} associato alla lingua.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Passa alla lingua successiva nell'ordine definito.
     * @return la lingua successiva.
     */
    public Language nextLanguage() {
        Language[] languages = values();
        int nextIndex = (this.ordinal() + 1) % languages.length;
        return languages[nextIndex];
    }
}
