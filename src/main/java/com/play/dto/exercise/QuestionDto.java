package com.play.dto.exercise;

import com.play.model.entity.exercise.Exercise;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object (DTO) per {@link com.play.model.entity.exercise.Question}.
 * Trasporta le informazioni di una domanda tra i livelli dell'applicazione.
 *
 * @param questionText Testo della domanda.
 * @param answers      Lista delle possibili risposte.
 */
public record QuestionDto(String questionText, List<AnswerDto> answers) implements Serializable {
}