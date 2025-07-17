package com.play.dao;

import com.play.util.provider.ConnectionDBUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe astratta generica per operazioni CRUD di base su entità.
 *
 * @param <T> Tipo dell'entità gestita dal DAO.
 */
public abstract class GenericDAO<T> {
    private static final Logger log = Logger.getLogger(GenericDAO.class.getName());

    /**
     * Ottiene una nuova sessione Hibernate dal SessionFactory.
     *
     * @return una nuova {@link Session}
     */
    protected Session getSession(){
        return ConnectionDBUtils.getSessionFactory().openSession();
    }

    /**
     * Salva una nuova entità nel database.
     *
     * @param entity entità da salvare
     * @throws HibernateException in caso di errore durante il salvataggio
     */
    public void save(T entity){
        Session session = null;
        Transaction tx = null;

        try {
            session = getSession();
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            log.log(Level.INFO, "Enitià salvata con successo: " + entity);
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Errore durante il salvataggio dell'entità: " + entity, ex);
            throw new HibernateException("Errore durante il salvataggio dell'entità", ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    /**
     * Aggiorna un'entità esistente nel database.
     *
     * @param entity entità da aggiornare
     * @throws HibernateException in caso di errore durante l'aggiornamento
     */
    public void update(T entity){
        Session session = null;
        Transaction tx = null;

        try {
            session = getSession();
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            log.log(Level.INFO, "Enitià aggiornata con successo: " + entity);
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Errore durante l'aggiornamento dell'entità: " + entity, ex);
            throw new HibernateException("Errore durante l'aggiornamento dell'entità", ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Elimina un'entità dal database.
     *
     * @param entity entità da eliminare
     * @throws HibernateException in caso di errore durante l'eliminazione
     */
    public void delete(T entity){
        Session session = null;
        Transaction tx = null;

        try {
            session = getSession();
            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
            log.log(Level.INFO, "Enitià eliminata con successo: " + entity);
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            log.log(Level.SEVERE, "Errore durante l'eliminazione dell'entità: " + entity, ex);
            throw new HibernateException("Errore durante l'eliminazione dell'entità", ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
