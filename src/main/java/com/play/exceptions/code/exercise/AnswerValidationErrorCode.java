package com.play.exceptions.code.exercise;

import com.play.exceptions.code.ErrorCode;

/**
 * Codici di errore per la validazione delle risposte di un esercizio.
 */
public enum AnswerValidationErrorCode implements ErrorCode {
    ANSWER_TEXT_INVALID("error.answer.text.invalid.messageKey");

    private final String messageKey;

    AnswerValidationErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }


    @Override
    public String getMessageKey() {
        return messageKey;
    }
}
