package com.play.model.entity.exercise;

import com.play.model.embedded.InfoExercise;
import com.play.model.entity.Score;
import com.play.model.enums.Difficulty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un esercizio composto da domande e associato a una difficoltà.
 * Contiene le informazioni generali e la lista delle domande e dei punteggi.
 */
@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exercise")
    private int idExercise;

    @Embedded
    private InfoExercise infoExercise;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    /**
     * Costruttore protetto per JPA.
     */
    protected Exercise() {
    }

    /**
     * Costruttore che utilizza l'ExcerciseBuilder.
     * @param builder il builder per l'esercizio
     */
    private Exercise(ExcerciseBuilder builder) {
        this.infoExercise = builder.infoExercise;
        this.difficulty = builder.difficulty;
        if (builder.questions != null) {
            for (Question q : builder.questions) {
                addQuestion(q);
            }
        }
    }

    /**
     * @return l'identificativo dell'esercizio
     */
    public int getIdExercise() {
        return idExercise;
    }

    /**
     * @return le informazioni generali dell'esercizio
     */
    public InfoExercise getInfoExercise() {
        return infoExercise;
    }

    /**
     * @return la difficoltà dell'esercizio
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return la lista delle domande dell'esercizio
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Aggiunge una domanda all'esercizio.
     * @param question la domanda da aggiungere
     */
    public void addQuestion(Question question) {
        if (question != null && !questions.contains(question)) {
            questions.add(question);
            question.setExercise(this);
        }
    }

    /**
     * Rimuove una domanda dall'esercizio.
     * @param question la domanda da rimuovere
     */
    public void removeQuestion(Question question) {
        if (question != null && questions.contains(question)) {
            questions.remove(question);
            if (question.getExercise() == this) {
                question.setExerciseOnly(null);
            }
        }
    }

    /**
     * @return la lista dei punteggi associati all'esercizio
     */
    public List<Score> getScores() {
        return scores;
    }

    /**
     * Aggiunge un punteggio all'esercizio.
     * @param score il punteggio da aggiungere
     */
    public void addScore(Score score) {
        if (score != null && !scores.contains(score)) {
            scores.add(score);
            if (score.getExercise() != this) {
                score.setExercise(this);
            }
        }
    }

    /**
     * Rimuove un punteggio dall'esercizio.
     * @param score il punteggio da rimuovere
     */
    public void removeScore(Score score) {
        if (score != null && scores.contains(score)) {
            scores.remove(score);
            if (score.getExercise() == this) {
                score.setExercise(null);
            }
        }
    }

    /**
     * Confronta due esercizi per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise exercise)) return false;
        if (idExercise != 0) {
            return idExercise == exercise.idExercise;
        }
        return infoExercise != null && infoExercise.equals(exercise.infoExercise);
    }

    /**
     * Calcola l'hash code per l'esercizio.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        if (idExercise != 0) {
            return Integer.hashCode(idExercise);
        }
        return infoExercise != null ? infoExercise.hashCode() : 0;
    }

    /**
     * Builder per la creazione di oggetti Exercise.
     */
    public static final class ExcerciseBuilder {
        private InfoExercise infoExercise;
        private Difficulty difficulty;
        private List<Question> questions = new ArrayList<>();

        /**
         * Imposta le informazioni generali dell'esercizio.
         * @param infoExercise le informazioni
         * @return il builder
         */
        public ExcerciseBuilder setInfoExercise(InfoExercise infoExercise) {
            this.infoExercise = infoExercise;
            return this;
        }

        /**
         * Imposta la difficoltà dell'esercizio.
         * @param difficulty la difficoltà
         * @return il builder
         */
        public ExcerciseBuilder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        /**
         * Imposta la lista delle domande.
         * @param questions la lista di domande
         * @return il builder
         */
        public ExcerciseBuilder setQuestions(List<Question> questions) {
            this.questions = questions != null ? questions : new ArrayList<>();
            return this;
        }

        /**
         * Aggiunge una domanda al builder.
         * @param question la domanda
         * @return il builder
         */
        public ExcerciseBuilder addQuestion(Question question) {
            if (question != null) {
                this.questions.add(question);
            }
            return this;
        }

        /**
         * Costruisce l'oggetto Exercise.
         * @return l'esercizio creato
         */
        public Exercise build() {
            return new Exercise(this);
        }
    }
}
