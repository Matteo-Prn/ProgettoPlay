package com.play.exceptions.validation.exercise;


import com.play.exceptions.code.exercise.AnswerValidationErrorCode;
import com.play.exceptions.validation.ValidationException;

public class AnswerValidationException extends ValidationException {

  public AnswerValidationException(AnswerValidationErrorCode errorCode) {
    super(errorCode);
  }

  public AnswerValidationException(AnswerValidationErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

  public AnswerValidationException(String message, AnswerValidationErrorCode errorCode) {
    super(message, errorCode);
  }
}
