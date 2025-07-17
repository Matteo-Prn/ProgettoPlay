package com.play.exceptions.code.user;

import com.play.exceptions.code.ErrorCode;


/**
 * Codici di errore per i servizi relativi agli utenti.
 */
public enum UserServiceErrorCode implements ErrorCode {

    ALREADY_EXISTS(
            "alreadyExists.messageKey",
            "alreadyExists.titleMessageKey"
    ),

    NOT_FOUND(
            "notFound.messageKey",
            "notFound.titleMessageKey"
    ),
    NOT_AUTHORIZED(
            "notAuthorized.messageKey",
            "notAuthorized.titleMessageKey"
    ),
    INVALID_PASSWORD(
            "invalidPassword.messageKey",
            "invalidPassword.titleMessageKey"
    );

    private final String messageKey;
    private final String titleMessageKey;


    UserServiceErrorCode(String messageKey, String titleMessageKey) {
        this.messageKey = messageKey;
        this.titleMessageKey = titleMessageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    public String getTitleMessageKey() {
        return titleMessageKey;
    }
}
