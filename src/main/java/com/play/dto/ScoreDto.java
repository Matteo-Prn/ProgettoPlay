package com.play.dto;

/**
 * Data Transfer Object (DTO) per trasferire le informazioni sul punteggio.
 *
 * @param userScore  Il punteggio ottenuto dall'utente.
 * @param totalScore Il punteggio massimo possibile per l'esercizio.
 */
public record ScoreDto(int userScore, int totalScore) {
}
