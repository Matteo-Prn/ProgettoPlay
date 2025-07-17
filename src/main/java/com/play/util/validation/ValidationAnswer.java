package com.play.util.validation;

import com.play.exceptions.code.exercise.AnswerValidationErrorCode;
import com.play.exceptions.validation.exercise.AnswerValidationException;
import com.play.util.provider.ConfigurationUtils;

/**
 * Utility per la validazione delle risposte degli esercizi.
 * Fornisce metodi statici per validare il testo della risposta
 * secondo le regole definite nei file di configurazione.
 * La classe non può essere istanziata.
 */
public final class ValidationAnswer {

    private static final String FILE_CONFIGURATION = "exerciseValidation.properties";

    private ValidationAnswer() {
        throw new UnsupportedOperationException("Utility class");
    }


    // Validazione del testo della risposta
    public static void validateText(String text) {
        if (text == null || text.isBlank()) {
            throw new AnswerValidationException(AnswerValidationErrorCode.ANSWER_TEXT_INVALID);
        }

        final String PATH_LENGTH = "answer.text.maxLength";
        final int MAX_LENGTH = ConfigurationUtils.getInt(FILE_CONFIGURATION, PATH_LENGTH);

        if (text.length() > MAX_LENGTH) {
            throw new AnswerValidationException(AnswerValidationErrorCode.ANSWER_TEXT_INVALID);
        }
    }
}
