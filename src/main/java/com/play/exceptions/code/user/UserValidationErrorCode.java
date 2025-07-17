package com.play.exceptions.code.user;

import com.play.exceptions.code.ErrorCode;

/**
 * Codici di errore per la validazione dei dati utente.
 */
public enum UserValidationErrorCode implements ErrorCode {

    FIRST_NAME_INVALID("firstName.messageKey",
            "firstName.emptyMessageKey"
    ),

    LAST_NAME_INVALID("lastName.messageKey",
            "lastName.emptyMessageKey"
    ),

    USERNAME_INVALID(
            "username.messageKey",
            "username.emptyMessageKey"
    ),

    PASSWORD_INVALID(
            "password.messageKey",
            "password.emptyMessageKey"
    );

    private final String messageKey;
    private final String emptyMessageKey;

    UserValidationErrorCode(String messageKey, String emptyMessageKey) {
        this.messageKey = messageKey;
        this.emptyMessageKey = emptyMessageKey;

    }


    @Override
    public String getMessageKey() {
        return messageKey;
    }

    public String getEmptyMessageKey() {
        return emptyMessageKey;
    }



}
