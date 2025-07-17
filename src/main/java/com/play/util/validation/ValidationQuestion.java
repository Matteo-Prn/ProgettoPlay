package com.play.util.validation;

import com.play.exceptions.code.exercise.QuestionValidateErrorCode;
import com.play.exceptions.validation.exercise.QuestionValidationException;
import com.play.util.provider.ConfigurationUtils;

/**
 * Utility per la validazione delle domande degli esercizi.
 * Fornisce metodi statici per validare il testo della domanda
 * secondo le regole definite nei file di configurazione.
 * La classe non può essere istanziata.
 */
public final class ValidationQuestion {

    private static final String FILE_CONFIGURATION = "exerciseValidation.properties";

    private ValidationQuestion() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Validazione del testo della domanda
    public static void validateQuestionText(String questionText) {
        if (questionText == null || questionText.isBlank()) {
            throw new QuestionValidationException(QuestionValidateErrorCode.QUESTION_TEXT_INVALID);

        }

        final String PATH_LENGTH = "question.text.maxLength";
        final int MAX_LENGTH = ConfigurationUtils.getInt(FILE_CONFIGURATION, PATH_LENGTH);
        if (questionText.length() > MAX_LENGTH) {
            throw new QuestionValidationException(QuestionValidateErrorCode.QUESTION_TEXT_INVALID);

        }
    }
}
