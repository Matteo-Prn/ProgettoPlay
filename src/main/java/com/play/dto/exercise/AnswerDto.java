package com.play.dto.exercise;

import com.play.model.entity.exercise.Question;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) per {@link Answer}.
 * Trasporta le informazioni di una risposta tra i livelli dell'applicazione.
 *
 * @param text      Testo della risposta.
 * @param isCorrect Indica se la risposta è corretta.
 */
public record AnswerDto(String text, boolean isCorrect) implements Serializable {
}