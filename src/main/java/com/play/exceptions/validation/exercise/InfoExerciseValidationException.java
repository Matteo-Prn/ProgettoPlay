package com.play.exceptions.validation.exercise;

import com.play.exceptions.code.exercise.ValidateInfoExerciseErrorCode;
import com.play.exceptions.validation.ValidationException;

public class InfoExerciseValidationException extends ValidationException {
    public InfoExerciseValidationException(ValidateInfoExerciseErrorCode errorCode) {
        super(errorCode);
    }

    public InfoExerciseValidationException(ValidateInfoExerciseErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public InfoExerciseValidationException(String message, ValidateInfoExerciseErrorCode errorCode) {
        super(message, errorCode);
    }
}
