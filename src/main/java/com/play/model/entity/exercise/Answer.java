package com.play.model.entity.exercise;

import jakarta.persistence.*;

/**
 * Rappresenta una risposta ad una domanda di un esercizio.
 * Ogni risposta è associata ad una {@link Question} e può essere corretta o meno.
 */
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_answer")
    private int idAnswer;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "id_question", nullable = false)
    private Question question;

    /**
     * Costruttore protetto per JPA.
     */
    protected Answer() {
    }

    /**
     * Costruttore che utilizza l'AnswerBuilder.
     * @param builder il builder per la risposta
     */
    private Answer(AnswerBuilder builder) {
        this.text = builder.text;
        this.isCorrect = builder.isCorrect;
    }

    /**
     * @return l'identificativo della risposta
     */
    public int getId_answer() {
        return idAnswer;
    }

    /**
     * @return il testo della risposta
     */
    public String getText() {
        return text;
    }

    /**
     * @return true se la risposta è corretta, false altrimenti
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    /**
     * @return la domanda associata a questa risposta
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Imposta la domanda associata a questa risposta.
     * @param question la domanda da associare
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * Confronta due risposte per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer answer)) return false;
        if (idAnswer != 0) {
            return idAnswer == answer.idAnswer;
        }
        return text != null && text.equals(answer.text) &&
                isCorrect == answer.isCorrect;
    }

    /**
     * Calcola l'hash code per la risposta.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        if (idAnswer != 0) {
            return Integer.hashCode(idAnswer);
        }
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (isCorrect ? 1 : 0);
        return result;
    }

    /**
     * Imposta la domanda senza gestire la relazione inversa.
     * @param question la domanda da associare
     */
    public void setQuestionOnly(Question question) {
        this.question = question;
    }

    /**
     * Builder per la creazione di oggetti Answer.
     */
    public static final class AnswerBuilder {
        private String text;
        private boolean isCorrect;

        /**
         * Imposta il testo della risposta.
         * @param text il testo
         * @return il builder
         */
        public AnswerBuilder setText(String text) {
            this.text = text;
            return this;
        }

        /**
         * Imposta se la risposta è corretta.
         * @param isCorrect true se corretta, false altrimenti
         * @return il builder
         */
        public AnswerBuilder setCorrect(boolean isCorrect) {
            this.isCorrect = isCorrect;
            return this;
        }

        /**
         * Costruisce l'oggetto Answer.
         * @return la risposta creata
         */
        public Answer build() {
            return new Answer(this);
        }
    }
}
