package com.play.exceptions.code.exercise;

import com.play.exceptions.code.ErrorCode;

/**
 * Codici di errore per la validazione delle informazioni di un esercizio.
 */
public enum ValidateInfoExerciseErrorCode implements ErrorCode {

    INFO_EXERCISE_TITLE_INVALID("info.exercise.invalid.title.messageKey"),
    INFO_EXERCISE_DESCRIPTION_INVALID("info.exercise.description.invalid.messageKey"),
    INFO_EXERCISE_CATEGORY_INVALID("info.exercise.category.invalid.messageKey"),
    INFO_EXERCISE_TOPIC_INVALID("info.exercise.topic.invalid.messageKey");

    private final String messageKey;

    ValidateInfoExerciseErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}
