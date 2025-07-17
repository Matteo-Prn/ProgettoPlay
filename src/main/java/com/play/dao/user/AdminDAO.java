package com.play.dao.user;

import com.play.dao.GenericDAO;
import com.play.dto.user.AdminDto;
import com.play.factory.UserFactory;
import com.play.model.entity.user.Admin;
import com.play.model.enums.Language;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.logging.Logger;

/**
 * DAO per la gestione delle operazioni sull'entità {@link Admin}.
 */
public class AdminDAO  extends GenericDAO<Admin> {

    private static final Logger log = Logger.getLogger(AdminDAO.class.getName());

    /**
     * Trova un amministratore tramite username.
     *
     * @param username username dell'amministratore
     * @return istanza di {@link Admin} se trovata, altrimenti null
     * @throws HibernateException in caso di errore durante la ricerca
     */
    public Admin findByUsername(String username) {
        try (Session session = getSession()) {
            return session.createQuery("FROM User WHERE username = :username", Admin.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            log.severe("Errore durante la ricerca dell'amministratore per username: " + e.getMessage());
            throw new HibernateException("Errore durante la ricerca dell'amministratore per username", e);
        }
    }

    /**
     * Salva un nuovo amministratore nel database.
     *
     * @param admin entità {@link Admin} da salvare
     * @throws HibernateException in caso di errore durante il salvataggio
     */
    public void save(Admin admin) {
        try (Session session = getSession()) {
            session.beginTransaction();
            session.persist(admin);
            session.getTransaction().commit();
            log.info("Admin salvato con successo: " + admin);
        } catch (Exception e) {
            log.severe("Errrore salvataggio admin: " + e.getMessage());
            throw new HibernateException("Errrore salvataggio admin", e);
        }
    }

    /**
     * Crea un amministratore predefinito se non esiste già.
     *
     * @throws HibernateException in caso di errore durante la creazione
     */
    public void createDefaultAdmin() {
      try (Session session = getSession()) {

          Admin existingAdmin = findByUsername("admin");

          if (existingAdmin == null) {

              AdminDto defaultAdminDto = new AdminDto(
                      "admin",
                      "admin123",
                      "Amministratore",
                      "Sistema",
                      Language.IT
              );

              Admin defaulAdmin = UserFactory.createAdmin(defaultAdminDto);
              save(defaulAdmin);
                log.info("Amministratore predefinito creato con successo");
          } else {
                log.info("Amministratore predefinito già esistente nel sistema");
          }
      } catch (Exception e) {
            log.severe("Errore durante la creazione dell'amministratore predefinito: " + e.getMessage());
            throw new HibernateException("Errore durante la creazione dell'amministratore predefinito", e);
        }
    }
}
