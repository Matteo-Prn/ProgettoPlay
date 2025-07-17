package com.play.exceptions.code.exercise;

import com.play.exceptions.code.ErrorCode;

/**
 * Codici di errore per i servizi relativi agli esercizi.
 */
public enum ExerciseServiceErrorCode implements ErrorCode {

    ALREADY_TITLE_EXISTS("error.already.title.exists.titleKey", null),
    EXERCISE_VALIDATION_FAILED("error.exercise.validation.failed.titleKey", "error.exercise.validation.failed.messageKey"),
    EXERCISE_SAVE_FAILED("error.exercise.save.failed.titleKey", "error.exercise.save.failed.messageKey"),
    EXERCISE_DELETE_FAILED("error.exercise.delete.failed.titleKey", "error.exercise.delete.failed.messageKey"),
    EXERCISE_NOT_FOUND("error.exercise.not.found.titleKey", "error.exercise.not.found.messageKey");

    private final String titleKey;
    private final String messageKey;

    ExerciseServiceErrorCode(String titleKey, String messageKey) {
        this.titleKey = titleKey;
        this.messageKey = messageKey;
    }


    @Override
    public String getMessageKey() {
        return messageKey;
    }

    public String getTitleKey() {
        return titleKey;
    }
}
