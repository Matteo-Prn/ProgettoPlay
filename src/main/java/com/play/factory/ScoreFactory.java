package com.play.factory;

import com.play.dto.ScoreDto;
import com.play.model.entity.Score;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.user.User;

/**
 * Factory per la creazione e conversione di entità {@link Score} e {@link ScoreDto}.
 */
public final class ScoreFactory {

    private ScoreFactory() {}

    /**
     * Crea una nuova istanza dell'entità {@link Score}.
     *
     * @param user L'utente.
     * @param exercise L'esercizio.
     * @param score Il punteggio.
     * @return una nuova entità {@link Score}.
     */
    public static Score createScore(User user, Exercise exercise, int score) {
        return new Score(user, exercise, score);
    }

    /**
     * Calcola il punteggio massimo per un esercizio.
     *
     * @param exercise L'esercizio.
     * @return Il punteggio massimo calcolabile.
     */
    public static int calculateTotalScore(Exercise exercise) {
        if (exercise == null || exercise.getQuestions() == null) {
            return 0;
        }
        int multiplier = exercise.getDifficulty().getMultiplier();
        return exercise.getQuestions().size() * multiplier;
    }

    /**
     * Crea un {@link ScoreDto} a partire da un'entità {@link Score} e un {@link Exercise}.
     *
     * @param score L'entità Score (può essere null se non esiste).
     * @param exercise L'esercizio di riferimento.
     * @return Un oggetto {@link ScoreDto}.
     */
    public static ScoreDto toDto(Score score, Exercise exercise) {
        int userScore = (score != null) ? score.getScore() : 0;
        int totalScore = calculateTotalScore(exercise);
        return new ScoreDto(userScore, totalScore);
    }
}
