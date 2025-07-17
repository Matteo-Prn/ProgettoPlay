package com.play.exceptions.validation.user;

import com.play.exceptions.code.user.UserValidationErrorCode;
import com.play.exceptions.validation.ValidationException;

public class UserValidationException extends ValidationException {

    public UserValidationException(UserValidationErrorCode errorCode) {
        super(errorCode);
    }

    public UserValidationException(UserValidationErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public UserValidationException(String message, UserValidationErrorCode errorCode) {
        super(message, errorCode);
    }
}
