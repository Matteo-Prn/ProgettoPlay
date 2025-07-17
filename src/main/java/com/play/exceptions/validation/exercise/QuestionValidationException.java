package com.play.exceptions.validation.exercise;

import com.play.exceptions.code.exercise.QuestionValidateErrorCode;
import com.play.exceptions.code.user.UserValidationErrorCode;
import com.play.exceptions.validation.ValidationException;

public class QuestionValidationException extends ValidationException {

    public QuestionValidationException(QuestionValidateErrorCode errorCode) {
        super(errorCode);
    }

    public QuestionValidationException(QuestionValidateErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public QuestionValidationException(String message, QuestionValidateErrorCode errorCode) {
        super(message, errorCode);
    }
}
