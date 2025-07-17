package com.play.exceptions.validation;

import com.play.exceptions.ApplicationException;
import com.play.exceptions.code.ErrorCode;

/**
 * ValidationException è una classe astratta che estende ApplicationException.
 * Rappresenta un'eccezione di base per il sistema di gestione delle eccezioni di Validazione.
 */
public abstract class ValidationException extends ApplicationException {

    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ValidationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ValidationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
