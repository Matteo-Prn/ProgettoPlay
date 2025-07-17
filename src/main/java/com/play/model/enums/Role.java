package com.play.model.enums;

/**
 * Questa Enum rappresenta i ruoli degli utenti
 */
public enum Role {

    STUDENT("student"),
    ADMIN("admin");

    private final String textUIKey;

    Role(String textUIKey) {
        this.textUIKey = textUIKey;
    }

    /**
     * @return la chiave per il testo dell'interfaccia utente.
     */
    public String getTextUIKey() {
        return textUIKey;
    }

}
