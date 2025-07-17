package com.play.model.entity.exercise;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una domanda all'interno di un esercizio.
 * Ogni domanda può avere più risposte e appartiene ad un {@link Exercise}.
 */
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_question")
    private int idQuestion;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_exercise", nullable = false)
    private Exercise exercise;

    /**
     * Costruttore protetto per JPA.
     */
    protected Question() {}

    /**
     * Costruttore che utilizza il QuestionBuilder.
     * @param builder il builder per la domanda
     */
    private Question(QuestionBuilder builder) {
        this.questionText = builder.questionText;
        if (builder.answers != null) {
            for (Answer a : builder.answers) {
                addAnswer(a);
            }
        }
    }

    /**
     * @return l'identificativo della domanda
     */
    public int getId_question() {
        return idQuestion;
    }

    /**
     * @return il testo della domanda
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * @return la lista delle risposte associate alla domanda
     */
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     * @return l'esercizio associato a questa domanda
     */
    public Exercise getExercise() {
        return exercise;
    }

    /**
     * Imposta l'esercizio associato a questa domanda.
     * @param exercise l'esercizio da associare
     */
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    /**
     * Aggiunge una risposta alla domanda.
     * @param answer la risposta da aggiungere
     */
    public void addAnswer(Answer answer) {
        if (answer != null && !answers.contains(answer)) {
            answers.add(answer);
            answer.setQuestion(this);
        }
    }

    /**
     * Rimuove una risposta dalla domanda.
     * @param answer la risposta da rimuovere
     */
    public void removeAnswer(Answer answer) {
        if (answer != null && answers.contains(answer)) {
            answers.remove(answer);
            if (answer.getQuestion() == this) {
                answer.setQuestionOnly(null);
            }
        }
    }

    /**
     * Imposta l'esercizio senza gestire la relazione inversa.
     * @param exercise l'esercizio da associare
     */
    public void setExerciseOnly(Exercise exercise) {
        this.exercise = exercise;
    }

    /**
     * Confronta due domande per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question question)) return false;
        if (idQuestion != 0) {
            return idQuestion == question.idQuestion;
        }
        return questionText != null && questionText.equals(question.questionText);
    }

    /**
     * Calcola l'hash code per la domanda.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        if (idQuestion != 0) {
            return Integer.hashCode(idQuestion);
        }
        return questionText != null ? questionText.hashCode() : 0;
    }

    /**
     * Builder per la creazione di oggetti Question.
     */
    public static final class QuestionBuilder {
        private String questionText;
        private List<Answer> answers = new ArrayList<>();

        /**
         * Imposta il testo della domanda.
         * @param questionText il testo
         * @return il builder
         */
        public QuestionBuilder setQuestionText(String questionText) {
            this.questionText = questionText;
            return this;
        }

        /**
         * Imposta la lista delle risposte.
         * @param answers la lista di risposte
         * @return il builder
         */
        public QuestionBuilder setAnswers(List<Answer> answers) {
            this.answers = answers != null ? answers : new ArrayList<>();
            return this;
        }

        /**
         * Aggiunge una risposta al builder.
         * @param answer la risposta
         * @return il builder
         */
        public QuestionBuilder addAnswer(Answer answer) {
            if (answer != null) {
                this.answers.add(answer);
            }
            return this;
        }

        /**
         * Costruisce l'oggetto Question.
         * @return la domanda creata
         */
        public Question build() {
            return new Question(this);
        }
    }
}
