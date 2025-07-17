package com.play.service;


import com.play.dao.user.AdminDAO;

import java.util.logging.Logger;

import com.play.dto.user.AdminDto;
import com.play.exceptions.ApplicationException;
import com.play.exceptions.code.user.UserServiceErrorCode;
import com.play.exceptions.service.user.UserServiceException;
import com.play.model.entity.user.Admin;
import com.play.util.validation.ErrorCollector;

/**
 * Service che gestisce le operazioni relative agli amministratori dell'applicazione.
 * Fornisce metodi per il login e la validazione delle credenziali dell'admin.
 */
public final class AdminService {

    private static final Logger LOG = Logger.getLogger(AdminService.class.getName());
    private static final AdminDAO ADMIN_DAO = new AdminDAO();
    private static final String DEFAULT_ADMIN_USERNAME = "admin";

    private AdminService() {
    }

    public static boolean loginAdmin(AdminDto adminDto) {
        return loginAdmin(adminDto, null);
    }

    /**
     * Effettua il login di un amministratore utilizzando le credenziali fornite.
     *
     * @param adminDto         Le credenziali dell'amministratore da autenticare.
     * @param errorCollector   Un oggetto per raccogliere eventuali errori durante il processo di login.
     * @return true se il login è riuscito, false altrimenti.
     */
    public static boolean loginAdmin(AdminDto adminDto, ErrorCollector errorCollector) {
        if (errorCollector == null) {
            errorCollector = new ErrorCollector();
        }

        try {
            if (!DEFAULT_ADMIN_USERNAME.equals(adminDto.username())) {
                errorCollector.addError(new UserServiceException(UserServiceErrorCode.NOT_AUTHORIZED));
                LOG.warning("Tentativo di accesso con nome utente non autorizzato: " + adminDto.username());
                return false;
            }

            Admin existingAdmin = ADMIN_DAO.findByUsername(adminDto.username());

            if (existingAdmin == null) {
                LOG.warning("Admin non trovato: " + adminDto.username());
                return false;
            }

            if (!existingAdmin.getPassword().equals(adminDto.password())) {
                errorCollector.addError(new UserServiceException(UserServiceErrorCode.INVALID_PASSWORD));
                LOG.warning("Password non valida per l'admin: " + adminDto.username());
                return false;
            }

            
            return true;
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                errorCollector.addError((ApplicationException) e);
            }
            LOG.severe("Errore durante la registrazione dello'amministratore: " + e.getMessage());
            return false;
        }
    }


}
