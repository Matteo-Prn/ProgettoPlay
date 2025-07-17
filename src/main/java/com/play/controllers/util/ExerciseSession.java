package com.play.controllers.util;

import com.play.model.entity.exercise.Answer;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.exercise.Question;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rappresenta lo stato di una sessione di gioco per un singolo esercizio.
 * Questo oggetto è transitorio e vive solo durante l'esecuzione dell'esercizio,
 * tenendo traccia delle domande, delle risposte dell'utente e dello stato di avanzamento.
 */
public final class ExerciseSession {

    /** L'esercizio in corso di svolgimento. */
    private final Exercise exercise;
    /** La lista delle domande dell'esercizio. */
    private final List<Question> questions;
    /** Mappa che associa ogni domanda alla risposta data dall'utente. */
    private final Map<Question, Answer> userAnswers;
    /** Indice della domanda corrente nella lista delle domande. */
    private int currentQuestionIndex;

    /**
     * Costruisce una nuova sessione di gioco per un dato esercizio.
     *
     * @param exercise L'esercizio da avviare. Non può essere nullo o senza domande.
     * @throws IllegalArgumentException se l'esercizio è nullo o non contiene domande.
     */
    public ExerciseSession(Exercise exercise) {
        if (exercise == null || exercise.getQuestions() == null || exercise.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("L'esercizio non può essere nullo o senza domande.");
        }
        this.exercise = exercise;
        this.questions = exercise.getQuestions();
        this.userAnswers = new HashMap<>();
        this.currentQuestionIndex = 0;
    }

    /**
     * Restituisce l'esercizio associato a questa sessione.
     *
     * @return L'oggetto {@link Exercise}.
     */
    public Exercise getExercise() {
        return exercise;
    }

    /**
     * Restituisce una lista non modificabile delle domande dell'esercizio.
     *
     * @return La lista delle {@link Question}.
     */
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    /**
     * Restituisce una mappa non modificabile delle risposte date dall'utente.
     *
     * @return La mappa delle risposte dell'utente.
     */
    public Map<Question, Answer> getUserAnswers() {
        return Collections.unmodifiableMap(userAnswers);
    }

    /**
     * Restituisce la domanda corrente.
     *
     * @return La {@link Question} corrente, o null se l'esercizio è terminato.
     */
    public Question getCurrentQuestion() {
        if (isFinished()) {
            return null;
        }
        return questions.get(currentQuestionIndex);
    }

    /**
     * Restituisce la risposta data dall'utente per la domanda corrente.
     *
     * @return L'{@link Answer} data, o null se non è stata data risposta.
     */
    public Answer getAnswerForCurrentQuestion() {
        return userAnswers.get(getCurrentQuestion());
    }

    /**
     * Registra la risposta data dall'utente per la domanda corrente.
     *
     * @param answer La risposta selezionata dall'utente. Può essere null se non viene data risposta.
     * @throws IllegalArgumentException se la risposta non appartiene alla domanda corrente.
     */
    public void recordAnswer(Answer answer) {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion != null) {
            if (answer != null && !currentQuestion.getAnswers().contains(answer)) {
                throw new IllegalArgumentException("La risposta non appartiene alla domanda corrente.");
            }
            userAnswers.put(currentQuestion, answer);
        }
    }

    /**
     * Avanza alla domanda successiva, se disponibile.
     *
     * @return true se c'è una domanda successiva, false altrimenti.
     */
    public boolean moveToNextQuestion() {
        if (!isLastQuestion()) {
            currentQuestionIndex++;
            return true;
        }
        return false;
    }

    /**
     * Torna alla domanda precedente, se possibile.
     *
     * @return true se è stato possibile tornare indietro, false altrimenti.
     */
    public boolean moveToPreviousQuestion() {
        if (canMoveToPreviousQuestion()) {
            currentQuestionIndex--;
            return true;
        }
        return false;
    }

    /**
     * Verifica se è possibile tornare alla domanda precedente.
     *
     * @return true se non si è sulla prima domanda, false altrimenti.
     */
    public boolean canMoveToPreviousQuestion() {
        return currentQuestionIndex > 0;
    }

    /**
     * Verifica se la domanda corrente è l'ultima dell'esercizio.
     *
     * @return true se è l'ultima domanda, false altrimenti.
     */
    public boolean isLastQuestion() {
        return currentQuestionIndex == questions.size() - 1;
    }

    /**
     * Verifica se l'esercizio è terminato (tutte le domande sono state superate).
     *
     * @return true se l'esercizio è finito, false altrimenti.
     */
    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    }

    /**
     * Restituisce il numero totale di domande nell'esercizio.
     *
     * @return Il numero totale di domande.
     */
    public int getTotalQuestions() {
        return questions.size();
    }

    /**
     * Restituisce il numero della domanda corrente (basato su 1).
     *
     * @return Il numero della domanda corrente.
     */
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    /**
     * Restituisce l'indice della domanda corrente (basato su 0).
     *
     * @return L'indice della domanda corrente.
     */
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    /**
     * Calcola le statistiche dell'esercizio (risposte corrette, errate, saltate).
     *
     * @return una mappa contenente il numero di risposte "correct", "incorrect", e "skipped".
     */
    public Map<String, Integer> getExerciseStats() {
        Map<String, Integer> stats = new HashMap<>();

        long correct = userAnswers.values().stream()
                .filter(answer -> answer != null && answer.isCorrect())
                .count();

        long incorrect = userAnswers.values().stream()
                .filter(answer -> answer != null && !answer.isCorrect())
                .count();

        long skipped = getTotalQuestions() - (correct + incorrect);

        stats.put("correct", (int) correct);
        stats.put("incorrect", (int) incorrect);
        stats.put("skipped", (int) skipped);

        return stats;
    }

    /**
     * Calcola il punteggio finale basandosi sulle risposte corrette e sulla difficoltà dell'esercizio.
     *
     * @return Il punteggio totale calcolato.
     */
    public int calculateScore() {
        long correctAnswers = userAnswers.values().stream()
                .filter(answer -> answer != null && answer.isCorrect())
                .count();

        int multiplier = exercise.getDifficulty().getMultiplier();
        return (int) correctAnswers * multiplier;
    }
}