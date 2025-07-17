package com.play.util.provider;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Utility per la gestione dell'internazionalizzazione (i18n).
 * Legge i messaggi localizzati dai file nella directory /i18n suddivisi per categoria e sottocartella.
 */
public final class I18NUtils {

    private static final Logger LOG = Logger.getLogger(I18NUtils.class.getName());
    private static final String BASE_PATH = "i18n";

    private static final String UI_SUBFOLDER = "ui";
    private static final String ENUM_SUBFOLDER = "enums";
    private static final String ERROR_SUBFOLDER = "error";
    private static final String EXERCISE_SUBFOLDER = "exercise";

    private I18NUtils() {
        throw new UnsupportedOperationException("Classe I18NUtils non può essere istanziata");
    }

    private static ResourceBundle getBundle(String category, String subfolder, Locale locale) {
        try {
            return ResourceBundle.getBundle(BASE_PATH + "." + category + "." + subfolder, locale);
        } catch (MissingResourceException e) {
            LOG.severe("Bundle non trovato per la categoria: " + category + ", sottocartella: " + subfolder + ", locale: " + locale);
            throw e;
        }
    }

    public static String get(String category, String subfolder, String key, Locale locale) {
        try {
            return getBundle(category, subfolder, locale).getString(key);
        } catch (MissingResourceException e) {
            LOG.warning("Chiave non trovata: " + key + " nella categoria: " + category + ", sottocartella: " + subfolder + ", locale: " + locale);
            throw e;
        }
    }

    /**
     * Tenta di tradurre una stringa se è una chiave i18n, altrimenti la restituisce così com'è.
     * Una stringa è considerata una chiave se contiene almeno un punto.
     *
     * @param bundleSubfolder La sottocartella del bundle (es. "exercise").
     * @param textOrKey La stringa che potrebbe essere una chiave o testo letterale.
     * @param locale Il locale per la traduzione.
     * @return La stringa tradotta o quella originale.
     */
    public static String translateIfKey(String bundleSubfolder, String textOrKey, Locale locale) {
        if (textOrKey == null || !textOrKey.contains(".")) {
            return textOrKey; // Non sembra una chiave, restituisci il testo originale.
        }
        try {
            // Assume che le chiavi degli esercizi siano nella categoria "exercise"
            return get(EXERCISE_SUBFOLDER, bundleSubfolder, textOrKey, locale);
        } catch (MissingResourceException e) {
            // La chiave non è stata trovata nel bundle, quindi la trattiamo come testo letterale.
            LOG.warning("Chiave '" + textOrKey + "' non trovata. Verrà visualizzata come testo letterale.");
            return textOrKey;
        }
    }

    public static String getEnum(String subfolder, String key, Locale locale) {
        return get(ENUM_SUBFOLDER, subfolder, key, locale);
    }

    public static String getUI(String subfolder, String key, Locale locale) {
        return get(UI_SUBFOLDER, subfolder, key, locale);
    }

    public static String getError(String subfolder, String key, Locale locale) {
        return get(ERROR_SUBFOLDER, subfolder, key, locale);
    }

    public static String getExerciseDefault(String subfolder, String key, Locale locale) {
        return get(EXERCISE_SUBFOLDER, subfolder, key, locale);
    }
}
