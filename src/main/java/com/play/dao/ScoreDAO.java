package com.play.dao;

import com.play.model.entity.Score;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.user.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * DAO per la gestione delle operazioni sull'entità {@link Score}.
 */
public class ScoreDAO extends GenericDAO<Score> {

    private static final Logger LOG = Logger.getLogger(ScoreDAO.class.getName());

    /**
     * Trova un punteggio in base all'utente e all'esercizio.
     * Poiché la combinazione di utente ed esercizio è univoca, questo metodo
     * restituirà al massimo un risultato.
     *
     * @param user     L'utente.
     * @param exercise L'esercizio.
     * @return un Optional contenente lo Score se trovato, altrimenti un Optional vuoto.
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public Optional<Score> findByUserAndExercise(User user, Exercise exercise) {
        try (Session session = getSession()) {
            return session.createQuery(
                            "FROM Score s WHERE s.user = :user AND s.exercise = :exercise", Score.class)
                    .setParameter("user", user)
                    .setParameter("exercise", exercise)
                    .uniqueResultOptional();
        } catch (Exception e) {
            LOG.severe("Errore durante la ricerca dello Score per utente ed esercizio: " + e.getMessage());
            throw new HibernateException("Errore durante la ricerca dello Score per utente ed esercizio", e);
        }
    }

    /**
     * Restituisce la lista dei punteggi associati a un utente.
     *
     * @param user L'utente.
     * @return lista di {@link Score}
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public List<Score> findByUser(User user) {
        try (Session session = getSession()) {
            return session.createQuery("FROM Score s JOIN FETCH s.exercise e JOIN FETCH e.infoExercise WHERE s.user = :user", Score.class)
                    .setParameter("user", user)
                    .list();
        } catch (Exception e) {
            LOG.severe("Errore durante la ricerca dei punteggi per utente: " + e.getMessage());
            throw new HibernateException("Errore durante la ricerca dei punteggi per utente", e);
        }
    }

    /**
     * Restituisce la lista di tutti i punteggi presenti nel database.
     *
     * @return lista di {@link Score}
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public List<Score> findAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM Score s JOIN FETCH s.exercise e JOIN FETCH e.infoExercise", Score.class)
                    .list();
        } catch (Exception e) {
            LOG.severe("Errore durante la ricerca di tutti i punteggi: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}