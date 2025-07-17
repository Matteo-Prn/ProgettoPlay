package com.play.model.entity.user;

import com.play.model.enums.Language;
import com.play.model.enums.Role;
import com.play.model.entity.Score;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che rappresenta un utente dell'applicazione.
 * Gestisce le informazioni comuni tra studenti e amministratori.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name ="role", nullable = false)
    protected Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    /**
     * Costruttore protetto per JPA.
     */
    protected User() {}

    /**
     * Costruttore protetto che utilizza il builder.
     * @param builder il builder per l'utente
     */
    protected User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.language = builder.language;
    }

    /**
     * @return l'identificativo dell'utente
     */
    public Integer getIdUser() {
        return idUser;
    }

    /**
     * @return lo username dell'utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return la password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return il nome dell'utente
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return il cognome dell'utente
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return il ruolo dell'utente
     */
    public Role getRole() {
        return role;
    }

    /**
     * @return la lingua preferita dell'utente
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @return la lista dei punteggi associati all'utente
     */
    public List<Score> getScores() {
        return scores;
    }

    /**
     * Aggiunge un punteggio all'utente.
     * @param score il punteggio da aggiungere
     */
    public void addScore(Score score) {
        if (score != null && !scores.contains(score)) {
            scores.add(score);
            if (score.getUser() != this) {
                score.setUser(this);
            }
        }
    }

    /**
     * Rimuove un punteggio dall'utente.
     * @param score il punteggio da rimuovere
     */
    public void removeScore(Score score) {
        if (score != null && scores.contains(score)) {
            scores.remove(score);
            if (score.getUser() == this) {
                score.setUser(null);
            }
        }
    }

    /**
     * Confronta due utenti per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        if (idUser != null) {
            return idUser.equals(user.idUser);
        }
        return username != null && username.equals(user.username);
    }

    /**
     * Calcola l'hash code per l'utente.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        if (idUser != null) {
            return idUser.hashCode();
        }
        return username != null ? username.hashCode() : 0;
    }

    /**
     * Builder astratto per la creazione di oggetti User.
     * @param <T> tipo del builder
     */
    public static abstract class UserBuilder<T extends UserBuilder<T>> {

        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private Language language = Language.IT;

        /**
         * Imposta lo username.
         * @param username lo username
         * @return il builder
         */
        public T setUsername(String username) {
            this.username = username;
            return self();
        }

        /**
         * Imposta la password.
         * @param password la password
         * @return il builder
         */
        public T setPassword(String password) {
            this.password = password;
            return self();
        }

        /**
         * Imposta il nome.
         * @param firstName il nome
         * @return il builder
         */
        public T setFirstName(String firstName) {
            this.firstName = firstName;
            return self();
        }

        /**
         * Imposta il cognome.
         * @param lastName il cognome
         * @return il builder
         */
        public T setLastName(String lastName) {
            this.lastName = lastName;
            return self();
        }

        /**
         * Imposta la lingua.
         * @param language la lingua
         * @return il builder
         */
        public T setLanguage(Language language) {
            this.language = language;
            return self();
        }

        /**
         * Restituisce il builder stesso.
         * @return il builder
         */
        protected abstract T self();

        /**
         * Costruisce l'oggetto User.
         * @return l'utente creato
         */
        public abstract User build();

    }
}
