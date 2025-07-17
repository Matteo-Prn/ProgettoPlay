package com.play.exceptions.service;

import com.play.exceptions.ApplicationException;
import com.play.exceptions.code.ErrorCode;

/**
 * ServiceException è una classe di eccezione che estende ApplicationException.
 * Rappresenta un'eccezione di base per il sistema di gestione delle eccezioni di un servizio.
 */
public class ServiceException extends ApplicationException {

    public ServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ServiceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
