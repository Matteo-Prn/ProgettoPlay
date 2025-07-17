package com.play.dto.exercise;

import com.play.model.enums.Category;
import com.play.model.enums.Topic;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) per {@link com.play.model.embedded.InfoExercise}.
 * Contiene le informazioni generali di un esercizio.
 *
 * @param title       Titolo dell'esercizio.
 * @param description Descrizione dell'esercizio.
 * @param category    Categoria dell'esercizio.
 * @param topic       Argomento dell'esercizio.
 */
public record InfoExerciseDto(String title, String description, Category category,
                              Topic topic) implements Serializable {
}