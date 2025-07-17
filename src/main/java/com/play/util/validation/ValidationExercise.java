package com.play.util.validation;

import com.play.exceptions.code.exercise.ExerciseValidationErrorCode;
import com.play.exceptions.validation.exercise.ExerciseValidationException;
import com.play.model.embedded.InfoExercise;
import com.play.model.entity.exercise.Question;
import com.play.model.enums.Difficulty;

import java.util.List;

/**
 * Utility per la validazione degli esercizi.
 * Fornisce metodi statici per validare le informazioni dell'esercizio,
 * le domande e la difficoltà, sollevando eccezioni in caso di dati non validi.
 * La classe non può essere istanziata.
 */
public final class ValidationExercise {

    private ValidationExercise() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Validazione delle informazioni dell'esercizio
   public static void validateInfoExercise(InfoExercise infoExercise) {
        if (infoExercise == null) {
            throw new ExerciseValidationException(ExerciseValidationErrorCode.EXERICSE_INFO_EMPTY);
        }
   }

   // Validazione delle domande dell'esercizio
   public static void validateQuestion(List<Question> questions) {
       if (questions == null || questions.isEmpty()) {
           throw new ExerciseValidationException(ExerciseValidationErrorCode.EXERCISE_QUESTION_EMPTY);
       }
   }

    // Validazione della difficoltà dell'esercizio
   public static void validateDifficulty(Difficulty difficulty) {
         if (difficulty == null) {
              throw new ExerciseValidationException(ExerciseValidationErrorCode.EXERCISE_DIFFICULTY_EMPTY);
         }
   }
}
