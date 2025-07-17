package com.play.util.validation;

import com.play.exceptions.code.exercise.ValidateInfoExerciseErrorCode;
import com.play.exceptions.validation.exercise.InfoExerciseValidationException;
import com.play.model.enums.Category;
import com.play.model.enums.Topic;
import com.play.util.provider.ConfigurationUtils;


/**
 * Utility per la validazione delle informazioni di un esercizio.
 * Fornisce metodi statici per validare titolo, descrizione, categoria e argomento
 * secondo le regole definite nei file di configurazione.
 * La classe non può essere istanziata.
 */
public final class ValidationInfoExercise {
    private ValidationInfoExercise() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final String FILE_CONFIGURATION = "exerciseValidation.properties";


    // Valida il titolo di un esercizio.
    public static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InfoExerciseValidationException(ValidateInfoExerciseErrorCode.INFO_EXERCISE_TITLE_INVALID);
        }

        final String PATH_LENGTH = "infoExercise.title.maxLength";
        final int MAX_LENGTH = ConfigurationUtils.getInt(FILE_CONFIGURATION, PATH_LENGTH);
        if (title.length() > MAX_LENGTH) {
            throw new InfoExerciseValidationException(ValidateInfoExerciseErrorCode.INFO_EXERCISE_TITLE_INVALID);
        }
    }

    // Valida la descrizione di un esercizio.
    public static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InfoExerciseValidationException(ValidateInfoExerciseErrorCode.INFO_EXERCISE_DESCRIPTION_INVALID);
        }

        final String PATH_LENGTH = "infoExercise.description.maxLength";
        final int MAX_LENGTH = ConfigurationUtils.getInt(FILE_CONFIGURATION, PATH_LENGTH);
        if (description.length() > MAX_LENGTH) {
            throw new InfoExerciseValidationException(ValidateInfoExerciseErrorCode.INFO_EXERCISE_DESCRIPTION_INVALID);
        }
    }

    // Valida la categoria di un esercizio.
    public static void validateCategory(Category category) {
        if (category == null) {
            throw new InfoExerciseValidationException(ValidateInfoExerciseErrorCode.INFO_EXERCISE_CATEGORY_INVALID);
        }
    }

    // Valida l'argomento di un esercizio.
    public static void validateTopic(Topic topic) {
        if (topic == null) {
            throw new InfoExerciseValidationException(ValidateInfoExerciseErrorCode.INFO_EXERCISE_TOPIC_INVALID);
        }
    }
}

