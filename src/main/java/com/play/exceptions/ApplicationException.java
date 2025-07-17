package com.play.exceptions;

import com.play.exceptions.code.ErrorCode;

/**
 * ApplicationException è una classe astratta che estende RuntimeException.
 * Rappresenta un'eccezione di base per il sistema di gestione delle eccezioni.
 */
public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(String message) {
        super(message);
        this.errorCode = null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }


}
