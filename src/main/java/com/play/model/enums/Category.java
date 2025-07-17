package com.play.model.enums;

/**
 * Enum che rappresenta le categorie di esercizi disponibili.
 * Ogni categoria ha una chiave associata per l'interfaccia utente.
 * FIND_ERROR: Categoria per esercizi di ricerca errori.
 * OUTPUT: Categoria per esercizi di output.
 * SOLVE_CODE: Categoria per esercizi di risoluzione del codice.
 */
public enum Category {
    FIND_ERROR("find_error"),
    OUTPUT("output"),
    SOLVE_CODE("solve_code");

    /**
     * Chiave testuale associata alla categoria per l'interfaccia utente.
     */
    private final String textUIKey;

    /**
     * Costruttore dell'enum Category.
     * @param textUIKey chiave testuale per l'interfaccia utente
     */
    Category(String textUIKey) {
        this.textUIKey = textUIKey;
    }

    /**
     * Restituisce la chiave testuale associata alla categoria.
     * @return chiave testuale per l'interfaccia utente
     */
    public String textUIKey() {
        return textUIKey;
    }

}

