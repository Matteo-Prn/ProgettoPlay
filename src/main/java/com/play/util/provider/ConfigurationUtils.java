package com.play.util.provider;

import java.util.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Utility per leggere valori dai file di configurazione nella directory /configuration.
 * Fornisce metodi statici per ottenere valori di tipo String, int, double e boolean dai file properties.
 * La classe non può essere istanziata.
 */
public final class ConfigurationUtils {

    private static final Logger LOG = Logger.getLogger(ConfigurationUtils.class.getName());
    private static final String CONFIG_DIR = "configuration/";

    private ConfigurationUtils() {
        throw new UnsupportedOperationException("Classe ConfigurationUtils non può essere istanziata");
    }

    /**
     * Restituisce il valore associato alla chiave come String.
     * @param fileName nome del file di configurazione
     * @param key chiave da cercare
     * @return valore come String
     * @throws NoSuchElementException se la chiave o il file non esistono
     */
    public static String getString(String fileName, String key) {
        Properties properties = loadProperties(fileName);
        String value = properties.getProperty(key);
        if (value == null) {
            LOG.severe("Configurazione non trovata per la chiave: " + key + " nel file: " + fileName);
            throw new NoSuchElementException();
        }
        return value;
    }

    /**
     * Restituisce il valore associato alla chiave come int.
     */
    public static int getInt(String fileName, String key) {
        String value = getString(fileName, key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Il valore '" + value + "' per la chiave '" + key + "' non è un numero intero valido", e);
        }
    }

//    /**
//     * Restituisce il valore associato alla chiave come double.
//     */
//    public static double getDouble(String fileName, String key) {
//        String value = getString(fileName, key);
//        try {
//            return Double.parseDouble(value);
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Il valore '" + value + "' per la chiave '" + key + "' non è un numero decimale valido", e);
//        }
//    }
//
//    /**
//     * Restituisce il valore associato alla chiave come boolean.
//     */
//    public static boolean getBoolean(String fileName, String key) {
//        String value = getString(fileName, key);
//        return Boolean.parseBoolean(value);
//    }

    /**
     * Carica le proprietà da un file di configurazione.
     * @param fileName nome del file di configurazione
     * @return Properties caricate dal file
     * @throws NoSuchElementException se il file non esiste
     */
    private static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        String path = CONFIG_DIR + fileName;

        try (InputStream input = ConfigurationUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                LOG.severe("File di configurazione non trovato:" + path);
                throw new NoSuchElementException();
            }
            properties.load(input);
        } catch (IOException e) {
            LOG.severe("Errore durante il caricamento del file di configurazione: " + path);
            throw new IllegalStateException();
        } catch (Exception e) {
            LOG.severe("Errore imprevisto durante il caricamento del file di configurazione: " + path);
            throw e;
        }

        return properties;
    }
}