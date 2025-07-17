package com.play.dao.exercise;

import com.play.dao.GenericDAO;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.exercise.Question;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;
import java.util.logging.Logger;

/**
 * DAO per la gestione delle operazioni sull'entità {@link Exercise}.
 */
public class ExerciseDAO extends GenericDAO<Exercise> {

    private static final Logger LOG = Logger.getLogger(ExerciseDAO.class.getName());

    @Override
    protected Session getSession() {
        return super.getSession();
    }


    /**
     * Trova un esercizio tramite il titolo.
     * Inizializza le domande e le risposte per evitare LazyInitializationException.
     *
     * @param title titolo dell'esercizio
     * @return istanza di {@link Exercise} se trovata, altrimenti null
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public Exercise findByTitle(String title) {
        try (Session session = getSession()) {
            Exercise exercise = session.createQuery("FROM Exercise e WHERE e.infoExercise.title = :title", Exercise.class)
                    .setParameter("title", title)
                    .uniqueResult();

            if (exercise != null) {
                // Inizializza la collezione di domande per evitare LazyInitializationException
                Hibernate.initialize(exercise.getQuestions());
                // Per ogni domanda, inizializza le risposte
                for (Question question : exercise.getQuestions()) {
                    Hibernate.initialize(question.getAnswers());
                }
            }
            return exercise;
        } catch(Exception e) {
            LOG.severe("Errore durante la ricerca dell'esercizio per titolo: " + e.getMessage());
            throw new HibernateException("Errore durante la ricerca dell'esercizio per titolo", e);
        }
    }

    /**
     * Restituisce la lista di tutti gli esercizi presenti nel database,
     * con domande e risposte inizializzate.
     *
     * @return lista di {@link Exercise}
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public List<Exercise> findAll() {
        try (Session session = getSession()) {
            // Step 1: Carica gli esercizi e le loro domande.
            // Usiamo DISTINCT per evitare duplicati di Exercise a causa del JOIN.
            List<Exercise> exercises = session.createQuery(
                    "SELECT DISTINCT e FROM Exercise e LEFT JOIN FETCH e.questions", Exercise.class
            ).list();

            // Step 2: Carica le risposte per le domande già caricate.
            if (!exercises.isEmpty()) {
                session.createQuery(
                        "SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.answers WHERE q.exercise IN (:exercises)",
                        com.play.model.entity.exercise.Question.class
                ).setParameter("exercises", exercises).list();
            }

            return exercises;
        } catch (Exception e) {
            LOG.severe("Errore durante la ricerca di tutti gli esercizi: " + e.getMessage());
            throw new HibernateException("Errore durante la ricerca di tutti gli esercizi", e);
        }
    }

    /**
     * Salva un nuovo esercizio nel database.
     *
     * @param exercise entità {@link Exercise} da salvare
     * @throws HibernateException in caso di errore durante il salvataggio
     */
    public void save(Exercise exercise) {
        try (Session session = getSession()) {
            session.beginTransaction();
            session.persist(exercise);
            session.getTransaction().commit();
            LOG.info("Esericzio salvato con successo: " + exercise);
        } catch (Exception e) {
            LOG.severe("Errore durante il salvataggio di un'eserczio: " + e.getMessage());
            throw new HibernateException("Errore durante il salvataggio di un'eserczio", e);
        }
    }

    /**
     * Elimina un esercizio dal database.
     *
     * @param exercise entità {@link Exercise} da eliminare
     * @throws HibernateException in caso di errore durante l'eliminazione
     */
    @Override
    public void delete(Exercise exercise) {
        try (Session session = getSession()) {
            session.beginTransaction();

            if (!session.contains(exercise)) {
                exercise = session.get(Exercise.class, exercise.getIdExercise());
            }
            if (exercise != null) {
                int id = exercise.getIdExercise();
                session.remove(exercise);
                session.getTransaction().commit();
                LOG.info("Esercizio eliminato con successo: " + id);
            } else {
                session.getTransaction().rollback();
            }
        } catch (Exception e) {
            LOG.severe("Errore durante l'eliminazione dell'esercizio: " + e.getMessage());
            throw new HibernateException("Errore durante l'eliminazione dell'esercizio", e);
        }
    }
}