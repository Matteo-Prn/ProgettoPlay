package com.play.exceptions.code.exercise;

import com.play.exceptions.code.ErrorCode;

/**
 * Codici di errore per la validazione delle domande di un esercizio.
 */
public enum QuestionValidateErrorCode implements ErrorCode {

    QUESTION_TEXT_INVALID("question.text.invalid.messageKey"),
    NO_CORRECT_ANSWER_SELECTED("question.no.correct.answer.selected.messageKey");


    private final String messageKey;

    QuestionValidateErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }


    @Override
    public String getMessageKey() {
        return messageKey;
    }


}
