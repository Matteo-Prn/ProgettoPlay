package com.play.util.validation;

import com.play.exceptions.code.user.UserValidationErrorCode;
import com.play.exceptions.validation.user.UserValidationException;
import com.play.util.provider.ConfigurationUtils;

/**
 * Utility per la validazione dei dati utente.
 * Fornisce metodi statici per validare nome, cognome, username e password
 * secondo le regole definite nei file di configurazione.
 * La classe non può essere istanziata.
 */
public final class UserValidation {

    private static final String FILE_CONFIGURATION = "userValidation.properties";

    private UserValidation() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Validazione del nome
    public static void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new UserValidationException(UserValidationErrorCode.FIRST_NAME_INVALID);
        }

        final String KEY_MIN_LENGTH = "firstAndLastName.minLength";
        final String KEY_MAX_LENGTH = "firstAndLastName.maxLength";
        final String KEY_PATTERN = "firstAndLastName.pattern";

        final int MIN_LENGTH = ConfigurationUtils.getInt(FILE_CONFIGURATION, KEY_MIN_LENGTH);
        final int MAX_LENGTH = ConfigurationUtils.getInt(FILE_CONFIGURATION, KEY_MAX_LENGTH);
        final String REGEX = ConfigurationUtils.getString(FILE_CONFIGURATION, KEY_PATTERN);

        if (firstName.length() < MIN_LENGTH || firstName.length() > MAX_LENGTH) {
            throw new UserValidationException(UserValidationErrorCode.FIRST_NAME_INVALID);
        }

        if (!firstName.matches(REGEX)) {
            throw new UserValidationException(UserValidationErrorCode.FIRST_NAME_INVALID);
        }
    }

    // Validazione del cognome
    public static void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new UserValidationException(UserValidationErrorCode.LAST_NAME_INVALID);
        }

        final int minLength = ConfigurationUtils.getInt(FILE_CONFIGURATION, "firstAndLastName.minLength");
        final int maxLength = ConfigurationUtils.getInt(FILE_CONFIGURATION, "firstAndLastName.maxLength");
        final String regex = ConfigurationUtils.getString(FILE_CONFIGURATION, "firstAndLastName.pattern");

        if (lastName.length() < minLength || lastName.length() > maxLength) {
            throw new UserValidationException(UserValidationErrorCode.LAST_NAME_INVALID);
        }

        if (!lastName.matches(regex)) {
            throw new UserValidationException(UserValidationErrorCode.LAST_NAME_INVALID);
        }
    }

    // Validazione dello username
    public static void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new UserValidationException(UserValidationErrorCode.USERNAME_INVALID);
        }

        final int minLength = ConfigurationUtils.getInt(FILE_CONFIGURATION, "username.minLength");
        final int maxLength = ConfigurationUtils.getInt(FILE_CONFIGURATION, "username.maxLength");
        final String regex = ConfigurationUtils.getString(FILE_CONFIGURATION, "username.pattern");

        if (username.length() < minLength || username.length() > maxLength) {
            throw new UserValidationException(UserValidationErrorCode.USERNAME_INVALID);
        }

        if (!username.matches(regex)) {
            throw new UserValidationException(UserValidationErrorCode.USERNAME_INVALID);
        }
    }

    // Validazione della password
    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new UserValidationException(UserValidationErrorCode.PASSWORD_INVALID);
        }

        final int minLength = ConfigurationUtils.getInt(FILE_CONFIGURATION, "password.minLength");
        final int maxLength = ConfigurationUtils.getInt(FILE_CONFIGURATION, "password.maxLength");
        final String regex = ConfigurationUtils.getString(FILE_CONFIGURATION, "password.pattern");

        if (password.length() < minLength || password.length() > maxLength) {
            throw new UserValidationException(UserValidationErrorCode.PASSWORD_INVALID);
        }

        if (!password.matches(regex)) {
            throw new UserValidationException(UserValidationErrorCode.PASSWORD_INVALID);
        }
    }
}
