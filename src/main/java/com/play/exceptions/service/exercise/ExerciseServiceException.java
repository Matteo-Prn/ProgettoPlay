package com.play.exceptions.service.exercise;

import com.play.exceptions.code.exercise.ExerciseServiceErrorCode;
import com.play.exceptions.service.ServiceException;

public class ExerciseServiceException extends ServiceException {
    public ExerciseServiceException(ExerciseServiceErrorCode exerciseServiceErrorCode) {
        super(exerciseServiceErrorCode);
    }

    public ExerciseServiceException(ExerciseServiceErrorCode exerciseServiceErrorCode, Throwable cause) {
        super(exerciseServiceErrorCode, cause);
    }

    public ExerciseServiceException(String message, ExerciseServiceErrorCode exerciseServiceErrorCode) {
        super(message, exerciseServiceErrorCode);
    }
}
