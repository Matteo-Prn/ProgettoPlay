package com.play.dao.user;

import com.play.dao.GenericDAO;
import com.play.model.entity.user.Student;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;
import java.util.logging.Logger;

/**
 * DAO per la gestione delle operazioni sull'entità {@link Student}.
 */
public class StudentDAO extends GenericDAO<Student> {

    private static final Logger log = Logger.getLogger(StudentDAO.class.getName());

    @Override
    protected Session getSession() {
        return super.getSession();
    }

    /**
     * Trova uno studente tramite username.
     *
     * @param username username dello studente
     * @return istanza di {@link Student} se trovata, altrimenti null
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public Student findByUsername(String username) {
        try (Session session = getSession()) {
            return session.createQuery("FROM User WHERE username = :username", Student.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            log.severe("Errorre durante la ricerca dell'username: " + e.getMessage());
            throw new HibernateException("Errorre durante la ricerca dellu studente traminte username", e);
        }
    }

    /**
     * Salva un nuovo studente nel database.
     *
     * @param student entità {@link Student} da salvare
     * @throws HibernateException in caso di errore durante il salvataggio
     */
    public void save(Student student) {
        try (Session session = getSession()) {
            session.beginTransaction();
            session.persist(student);
            session.getTransaction().commit();
            log.info("Studente salvato con successo: " + student);
        } catch (Exception e) {
            log.severe("Errore salvataggio studente: " + e.getMessage());
            throw new HibernateException("Errore salvataggio studente", e);
        }
    }

    /**
     * Conta il numero totale di studenti presenti nel database.
     *
     * @return numero di studenti
     * @throws HibernateException in caso di errore durante il conteggio
     */
    public int countAll() {
        try (Session session = getSession()) {
            return session.createQuery("SELECT count(s) FROM Student s", Long.class)
                    .getSingleResult().intValue();
        } catch (Exception e) {
            log.severe("Errore nel contare gli studenti: " + e.getMessage());
            throw new HibernateException("Errore nel contare gli studenti", e);
        }
    }

    /**
     * Restituisce la lista di tutti gli studenti presenti nel database.
     *
     * @return lista di {@link Student}
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public List<Student> findAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            log.severe("Errore nella ricerca di tutti gli studenti: " + e.getMessage());
            throw new HibernateException("Errore nella ricerca di tutti gli studenti", e);
        }
    }

}


