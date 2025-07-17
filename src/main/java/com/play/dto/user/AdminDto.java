package com.play.dto.user;

import com.play.model.entity.user.Admin;
import com.play.model.enums.Language;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) per {@link Admin}.
 * Trasporta le informazioni dell'amministratore tra i livelli dell'applicazione.
 *
 * @param username  Nome utente dell'amministratore.
 * @param password  Password dell'amministratore.
 * @param firstName Nome dell'amministratore.
 * @param lastName  Cognome dell'amministratore.
 * @param language  Lingua preferita dell'amministratore.
 */
public record AdminDto(String username, String password, String firstName, String lastName, Language language) implements Serializable {
}