package com.play.model.entity.user;

import com.play.model.enums.Role;

import jakarta.persistence.*;

/**
 * Rappresenta un utente amministratore dell'applicazione.
 */
@Entity
@PrimaryKeyJoinColumn(name = "id_user")
public class Admin extends User {

    /**
     * Costruttore protetto per JPA.
     */
    protected Admin() {}

    /**
     * Costruttore che utilizza l'AdminBuilder.
     * @param builder il builder per l'amministratore
     */
    public Admin (AdminBuilder builder) {
        super(builder);
        this.role = Role.ADMIN;
    }

    /**
     * Confronta due amministratori per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        return super.equals(o);
    }

    /**
     * Calcola l'hash code per l'amministratore.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Builder per la creazione di oggetti Admin.
     */
    public static final class AdminBuilder extends UserBuilder<AdminBuilder> {

        @Override
        protected AdminBuilder self() {
            return this;
        }

        /**
         * Costruisce l'oggetto Admin.
         * @return l'amministratore creato
         */
        @Override
        public Admin build() {
            return new Admin(this);
        }
    }
}