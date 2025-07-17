package com.play.model.entity.user;
import com.play.model.enums.Role;

import jakarta.persistence.*;

/**
 * Rappresenta uno studente dell'applicazione.
 */
@Entity
@PrimaryKeyJoinColumn(name = "id_user")
public class Student extends User {

    /**
     * Costruttore protetto per JPA.
     */
    protected Student() {}

    /**
     * Costruttore che utilizza lo StudentBuilder.
     * @param builder il builder per lo studente
     */
    private Student(StudentBuilder builder) {
        super(builder);
        this.role = Role.STUDENT;
    }

    /**
     * Confronta due studenti per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return super.equals(o);
    }

    /**
     * Calcola l'hash code per lo studente.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Builder per la creazione di oggetti Student.
     */
    public static final class StudentBuilder extends UserBuilder<StudentBuilder> {

        @Override
        protected StudentBuilder self() {
            return this;
        }

        /**
         * Costruisce l'oggetto Student.
         * @return lo studente creato
         */
        @Override
        public Student build() {
            return new Student(this);
        }
    }

}
