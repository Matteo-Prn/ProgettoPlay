package com.play.dto;

import com.play.model.entity.user.Student;

/**
 * Data Transfer Object (DTO) per rappresentare la classifica degli studenti.
 *
 * @param rank   Posizione in classifica dello studente.
 * @param student Istanza dello studente.
 * @param score  Punteggio ottenuto dallo studente.
 */
public record RankTable(int rank, Student student, int score) {
}
