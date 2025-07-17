package com.play.dto.exercise;

import com.play.model.entity.exercise.Exercise;
import com.play.model.enums.Difficulty;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object (DTO) per {@link Exercise}.
 * Trasporta le informazioni di un esercizio tra i livelli dell'applicazione.
 *
 * @param infoExercise Informazioni generali sull'esercizio.
 * @param difficulty   Difficoltà dell'esercizio.
 * @param questions    Lista delle domande dell'esercizio.
 */
public record ExerciseDto(InfoExerciseDto infoExercise, Difficulty difficulty,
                          List<QuestionDto> questions) implements Serializable {
}