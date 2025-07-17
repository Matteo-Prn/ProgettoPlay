package com.play.exceptions.validation.exercise;

import com.play.exceptions.code.exercise.ExerciseValidationErrorCode;
import com.play.exceptions.validation.ValidationException;

public class ExerciseValidationException extends ValidationException {
  public ExerciseValidationException(ExerciseValidationErrorCode errorCode) {
    super(errorCode);
  }

  public ExerciseValidationException(ExerciseValidationErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

  public ExerciseValidationException(String message, ExerciseValidationErrorCode errorCode) {
    super(message, errorCode);
  }
}
