package com.play.model.entity;

import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.user.User;
import jakarta.persistence.*;

/**
 * Rappresenta il punteggio ottenuto da un utente su un esercizio specifico.
 * Ogni istanza collega un {@link User} ad un {@link Exercise} con il relativo punteggio.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_user", "id_exercise"}))
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exercise", nullable = false)
    private Exercise exercise;

    private int score;

    /**
     * Costruttore protetto per JPA.
     */
    protected Score() {}

    /**
     * Crea un nuovo punteggio associato ad un utente e ad un esercizio.
     * @param user l'utente che ha ottenuto il punteggio
     * @param exercise l'esercizio svolto
     * @param score il punteggio ottenuto
     */
    public Score(User user, Exercise exercise, int score) {
        this.user = user;
        this.exercise = exercise;
        this.score = score;
    }

    /**
     * @return l'identificativo del punteggio
     */
    public int getIdScore() {
        return idScore;
    }

    /**
     * @return l'utente associato al punteggio
     */
    public User getUser() {
        return user;
    }

    /**
     * Imposta l'utente associato al punteggio.
     * @param user l'utente da associare
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return l'esercizio associato al punteggio
     */
    public Exercise getExercise() {
        return exercise;
    }

    /**
     * Imposta l'esercizio associato al punteggio.
     * @param exercise l'esercizio da associare
     */
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    /**
     * @return il valore del punteggio
     */
    public int getScore() {
        return score;
    }

    /**
     * Imposta il valore del punteggio.
     * @param score il nuovo valore del punteggio
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Confronta se questo punteggio è migliore di un altro.
     * @param other il punteggio da confrontare
     * @return true se questo punteggio è maggiore, false altrimenti
     */
    public boolean isBetterThan(Score other) {
        return this.score > other.score;
    }

    /**
     * Confronta due oggetti Score per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score score)) return false;

        if (idScore != 0) {
            return idScore == score.idScore;
        }

        return user != null && exercise != null &&
                user.equals(score.user) &&
                exercise.equals(score.exercise);
    }

    /**
     * Calcola l'hash code per l'oggetto Score.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        if (idScore != 0) {
            return Integer.hashCode(idScore);
        }

        int result = 17;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (exercise != null ? exercise.hashCode() : 0);
        return result;
    }
}
