package com.play.util.provider;

import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * ConnectionDBUtils è una classe di utilità per gestire la connessione al database tramite Hibernate.
 * Fornisce un singleton SessionFactory e metodi per ottenere e chiudere la sessione.
 * La classe non può essere istanziata.
 */
public final class ConnectionDBUtils {

    private ConnectionDBUtils() {
        throw new UnsupportedOperationException("Classe ConnectionDBUtils non può essere istanziata");
    }

    private static final Logger logger = Logger.getLogger(ConnectionDBUtils.class.getName());
    private static final SessionFactory sessionFactory;

    /**
     * Inizializza la SessionFactory una sola volta al caricamento della classe.
     * Utilizza il file di configurazione hibernate.cfg.xml presente nella cartella resources.
     */
    static {
        try {
            sessionFactory = new Configuration()
                    .configure() // legge il file di configurazione src/main/resources/hibernate.cfg.xml
                    .buildSessionFactory();
        } catch (RuntimeException ex) {
            logger.severe("Inizializzazione della SessionFactory fallita");
            throw new RuntimeException("Errore durante l'inizializzazione di Hibernate", ex);
        }
    }

    /**
     * Restituisce la SessionFactory singleton per gestire le sessioni Hibernate.
     *
     * @return la SessionFactory condivisa
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Chiude la SessionFactory se non è già chiusa.
     * Da chiamare alla chiusura dell'applicazione per liberare le risorse.
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("SessionFactory chiusa correttamente.");
        } else {
            logger.severe("Tentativo di chiudere una SessionFactory già chiusa o nulla.");
        }
    }
}
