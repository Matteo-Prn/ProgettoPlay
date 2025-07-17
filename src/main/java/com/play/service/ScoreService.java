package com.play.service;

import com.play.dao.ScoreDAO;
import com.play.dto.ScoreDto;
import com.play.factory.ScoreFactory;
import com.play.model.entity.Score;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.user.User;
import com.play.model.enums.Category;
import org.hibernate.HibernateException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service che gestisce i punteggi degli utenti relativi agli esercizi.
 * Permette di salvare, aggiornare e recuperare i punteggi, oltre a statistiche sui risultati.
 */
public final class ScoreService {

    private static final Logger LOG = Logger.getLogger(ScoreService.class.getName());
    private static final ScoreDAO SCORE_DAO = new ScoreDAO();

    private ScoreService() {}

    /**
     * Salva o aggiorna il punteggio di un utente per un esercizio.
     * Se l'utente non ha un punteggio per questo esercizio, ne viene creato uno nuovo.
     * Se esiste già un punteggio, viene aggiornato solo se quello nuovo è migliore.
     *
     * @param user L'utente che ha completato l'esercizio.
     * @param exercise L'esercizio completato.
     * @param newScore Il nuovo punteggio ottenuto.
     */
    public static void saveOrUpdateBestScore(User user, Exercise exercise, int newScore) {
        if (user == null || exercise == null) {
            LOG.warning("Tentativo di salvare un punteggio con utente o esercizio nullo.");
            return;
        }

        try {
            Optional<Score> existingScoreOpt = SCORE_DAO.findByUserAndExercise(user, exercise);

            if (existingScoreOpt.isPresent()) {
                // Esiste già un punteggio, aggiorna solo se quello nuovo è migliore
                Score existingScore = existingScoreOpt.get();
                if (newScore > existingScore.getScore()) {
                    existingScore.setScore(newScore);
                    SCORE_DAO.update(existingScore);
                    LOG.info("Punteggio aggiornato per l'utente " + user.getUsername() + " per l'esercizio " + exercise.getIdExercise());
                } else {
                    LOG.info("Il nuovo punteggio non è migliore di quello esistente. Nessun aggiornamento.");
                }
            } else {
                // Nessun punteggio esistente, crea uno nuovo
                Score score = ScoreFactory.createScore(user, exercise, newScore);
                SCORE_DAO.save(score);
                LOG.info("Nuovo punteggio salvato per l'utente " + user.getUsername() + " per l'esercizio " + exercise.getIdExercise());
            }
        } catch (HibernateException e) {
            LOG.severe("Errore del database durante il salvataggio o l'aggiornamento del punteggio: " + e.getMessage());
        }
    }

    /**
     * Recupera il DTO del punteggio per un dato utente ed esercizio.
     *
     * @param user L'utente.
     * @param exercise L'esercizio.
     * @return Un ScoreDto che rappresenta il punteggio dell'utente e quello totale.
     */
    public static ScoreDto getScoreForUserAndExercise(User user, Exercise exercise) {
        try {
            Optional<Score> scoreOpt = SCORE_DAO.findByUserAndExercise(user, exercise);
            return ScoreFactory.toDto(scoreOpt.orElse(null), exercise);
        } catch (HibernateException e) {
            LOG.severe("Errore del database durante il recupero del punteggio: " + e.getMessage());
            // Restituisce un DTO con punteggio 0 in caso di errore per non bloccare la UI
            return ScoreFactory.toDto(null, exercise);
        }
    }

    /**
     * Recupera il punteggio totale di un utente, sommando tutti i punteggi ottenuti.
     *
     * @param user L'utente di cui calcolare il punteggio totale.
     * @return Il punteggio totale dell'utente.
     */
    public static int getTotalScoreForUser(User user) {
        try {
            List<Score> scores = SCORE_DAO.findByUser(user);
            return scores.stream().mapToInt(Score::getScore).sum();
        } catch (HibernateException e) {
            LOG.severe("Errore nel calcolare il punteggio totale per l'utente " + user.getUsername() + ": " + e.getMessage());
            return 0;
        }
    }

    /**
     * Conta il numero di esercizi completati da un utente.
     *
     * @param user L'utente di cui contare gli esercizi completati.
     * @return Il numero di esercizi completati dall'utente.
     */
    public static int countCompletedExercisesByUser(User user) {
        try {
            return SCORE_DAO.findByUser(user).size();
        } catch (HibernateException e) {
            LOG.severe("Errore nel contare gli esercizi completati per l'utente " + user.getUsername() + ": " + e.getMessage());
            return 0;
        }
    }

    /**
     * Recupera tutti i punteggi di un utente.
     *
     * @param user L'utente di cui recuperare i punteggi.
     * @return Una lista di Score che rappresentano i punteggi dell'utente.
     */
    public static List<Score> getScoresForUser(User user) {
        try {
            return SCORE_DAO.findByUser(user);
        } catch (HibernateException e) {
            LOG.severe("Errore nel recuperare i punteggi per l'utente " + user.getUsername() + ": " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Recupera il numero totale di esercizi disponibili.
     *
     * @return Il numero totale di esercizi.
     */
    public static int getTotalExerciseCount() {
        return ExerciseService.getAllExercises().size();
    }

    /**
     * Recupera la categoria più giocata tra tutti gli esercizi completati.
     *
     * @return Un Optional contenente la categoria più giocata, o vuoto se non ci sono punteggi.
     */
    public static Optional<Category> getMostPlayedCategory() {
        try {
            List<Score> allScores = SCORE_DAO.findAll();
            if (allScores.isEmpty()) {
                return Optional.empty();
            }

            return allScores.stream()
                    .map(score -> score.getExercise().getInfoExercise().getCategory())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);
        } catch (HibernateException e) {
            LOG.severe("Errore nel calcolare la categoria più giocata: " + e.getMessage());
            return Optional.empty();
        }
    }
}
