package com.play.controllers.util;

import com.play.dao.user.AdminDAO;
import com.play.model.entity.user.User;
import com.play.model.enums.Language;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.logging.Logger;

/**
 * Gestisce la sessione globale dell'applicazione utilizzando il pattern Singleton.
 * Mantiene lo stato dell'utente autenticato e la lingua corrente,
 * fornendo property JavaFX.
 */
public final class SessionManager {

    private static final Logger LOG = Logger.getLogger(SessionManager.class.getName());

    /** L'unica istanza della classe SessionManager. */
        private static SessionManager instance;

    /** Property che contiene la lingua corrente dell'applicazione. */
        private ObjectProperty<Language> currentLanguage = new SimpleObjectProperty<>(Language.IT);

    /** Property che contiene l'utente attualmente autenticato. */
        private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>(null);

        private static final AdminDAO ADMIN_DAO = new AdminDAO();

    /**
     * Costruttore privato per implementare il pattern Singleton.
     * Assicura che l'amministratore di default esista nel sistema.
     */
        private SessionManager() {
            ensureDefaultAdminExists();
        }

    /**
     * Restituisce l'unica istanza di SessionManagers.
     *
     * @return L'istanza singleton di {@link SessionManager}.
     */
        public static synchronized SessionManager getInstance() {
            if (instance == null) {
                instance = new SessionManager();
            }
            return instance;
        }

    /**
     * Restituisce la lingua attualmente impostata nell'applicazione.
     *
     * @return La {@link Language} corrente.
     */
    public Language getCurrentLanguage() {
        return currentLanguage.get();
    }


    /**
     * Imposta la lingua corrente dell'applicazione.
     *
     * @param language La nuova {@link Language} da impostare.
     */
    public void setCurrentLanguage(Language language) {
        currentLanguage.set(language);
    }

    /**
     * Restituisce la property JavaFX per la lingua corrente.
     * Utile per osservare i cambiamenti della lingua.
     *
     * @return L'{@link ObjectProperty} della lingua corrente.
     */
    public ObjectProperty<Language> currentLanguageProperty() {
        return currentLanguage;
    }

    /**
     * Restituisce l'utente attualmente autenticato.
     *
     * @return L'{@link User} corrente, o null se nessun utente è loggato.
     */
    public User getCurrentUser() {
        return currentUser.get();
    }

    /**
     * Imposta l'utente corrente della sessione.
     *
     * @param user L'{@link User} che ha effettuato l'accesso.
     */
    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    /**
     * Restituisce la property JavaFX per l'utente corrente.
     * Utile per osservare i cambiamenti dello stato di login.
     *
     * @return L'{@link ObjectProperty} dell'utente corrente.
     */
    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    /**
     * Verifica se un utente è attualmente autenticato nella sessione.
     *
     * @return true se un utente ha effettuato il login, false altrimenti.
     */
    public boolean isUserLoggedIn() {
        return currentUser.get() != null;
    }

    /**
     * Disconnette l'utente corrente
     */
    public void logout() {
        currentUser.set(null);
    }

    /**
     * Metodo per assicurare che l'amministratore di default esista
     * all'avvio della sessione.
     */
    private void ensureDefaultAdminExists() {
        try {
            ADMIN_DAO.createDefaultAdmin();
            LOG.info("Verifica amministratore di default completata");
        } catch (Exception e) {
            LOG.severe("Errore durante la verifica/creazione dell'amministratore di default: " + e.getMessage());
        }
    }

    }

