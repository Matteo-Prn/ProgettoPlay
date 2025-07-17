package com.play.exceptions.code.exercise;

import com.play.exceptions.code.ErrorCode;

/**
 * Codici di errore per la validazione degli esercizi.
 */
public enum ExerciseValidationErrorCode implements ErrorCode {
    EXERICSE_INFO_EMPTY(null),
    EXERCISE_QUESTION_EMPTY(null),
    EXERCISE_DIFFICULTY_EMPTY("exercise.difficulty.empty.messageKey");

    private final String messageKey;

    ExerciseValidationErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }


    @Override
    public String getMessageKey() {
        return messageKey;
    }
}
