package com.play.util.validation;
import com.play.exceptions.ApplicationException;
import com.play.exceptions.code.ErrorCode;

import java.util.*;
import java.util.logging.Logger;

/**
 * Raccoglitore di errori di validazione.
 * Permette di aggiungere, ottenere e gestire eccezioni di validazione
 * e i relativi codici di errore. Fornisce metodi per la pulizia e il conteggio degli errori.
 * La classe non può essere estesa.
 */
public final class ErrorCollector {

    private static final Logger log = Logger.getLogger(ErrorCollector.class.getName());
    private final List<ApplicationException> errors = new ArrayList<>();
    private final Map<Object, ApplicationException> errorMap = new LinkedHashMap<>();



    /**
     * Aggiunge un'eccezione alla collezione di errori
     *
     * @param exception l'eccezione da aggiungere
     */
    public void addError(ApplicationException exception) {
        log.warning("Aggiunto errore di validazione: " + exception.getMessage());
        errors.add(exception);
    }

    public void addError(Object key, ApplicationException exception) {
        log.warning("Aggiunyto errore di validazione per la chiave '" + key +"':" + exception);
        errorMap.put(key, exception);
    }

    /**
     * Verifica se sono presenti errori nel raccoglitore
     *
     * @return true se ci sono errori, false altrimenti
     */
    public boolean hasErrors() {
        return !errors.isEmpty() || !errorMap.isEmpty();
    }

    /**
     * Ottiene la lista immutabile delle eccezioni raccolte
     *
     * @return lista di eccezioni
     */
    public List<ApplicationException> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public Map<Object, ApplicationException> errorMap() {
        return Collections.unmodifiableMap(errorMap);
    }

    /**
     * Ottiene la lista degli ErrorCode associati alle eccezioni
     *
     * @return lista di codici di errore
     */
    public List<ErrorCode> getErrorCodes() {
        List<ErrorCode> errorCodes = new ArrayList<>();
        for (ApplicationException exception : errors) {
            if (exception.getErrorCode() != null) {
                errorCodes.add(exception.getErrorCode());
            }
        }
        return errorCodes;
    }

    /**
     * Pulisce tutti gli errori raccolti
     */
    public void clear() {
        errorMap.clear();
        errors.clear();
    }

    /**
     * Restituisce il numero di errori raccolti
     *
     * @return il conteggio degli errori
     */
    public int getErrorCount() {
        return errors.size();
    }

}
