package com.play.dto.user;

import com.play.model.entity.user.Student;
import com.play.model.enums.Language;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) per {@link Student}.
 * Trasporta le informazioni dello studente tra i livelli dell'applicazione.
 *
 * @param username  Nome utente dello studente.
 * @param password  Password dello studente.
 * @param firstName Nome dello studente.
 * @param lastName  Cognome dello studente.
 * @param language  Lingua preferita dello studente.
 */
public record StudentDto(String username, String password, String firstName, String lastName,
                         Language language) implements Serializable {
}
