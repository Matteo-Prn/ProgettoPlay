package com.play.model.enums;

import com.play.util.provider.ConfigurationUtils;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * Questa classe rappresenta i diversi livelli di difficoltà degli esercizi.
 * Il moltiplicatore di punteggio viene caricato dal file di configurazione.
 */
public enum Difficulty {

    EASY("easy", 1, "difficulty.easy.multiplier", 1),
    INTERMEDIATE("intermediate", 2, "difficulty.intermediate.multiplier", 2),
    HARD("hard", 3, "difficulty.hard.multiplier", 3);

    private static final Logger LOG = Logger.getLogger(Difficulty.class.getName());
    private static final String CONFIG_FILE = "exerciseValidation.properties";

    private final String textUIKey;
    private final int level;
    private final int multiplier;

    Difficulty(String textUIKey, int level, String configKey, int defaultMultiplier) {
        this.textUIKey = textUIKey;
        this.level = level;
        this.multiplier = loadMultiplier(this.name(), configKey, defaultMultiplier);
    }

    private static int loadMultiplier(String enumName, String configKey, int defaultMultiplier) {
        try {
            return ConfigurationUtils.getInt(CONFIG_FILE, configKey);
        } catch (NoSuchElementException | IllegalStateException e) {
            LOG.severe("Impossibile caricare il moltiplicatore per la difficolta");
            return defaultMultiplier;
        }
    }

    /**
     * @return la chiave per il testo dell'interfaccia utente.
     */
    public String getTextUIKey() {
        return textUIKey;
    }

    /**
     * @return il livello di difficoltà numerico.
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return il moltiplicatore di punteggio per questa difficoltà.
     */
    public int getMultiplier() {
        return multiplier;
    }
}