package com.play.util.db;

import com.play.service.DefaultExerciseInitializer;
import com.play.util.provider.ConnectionDBUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;

/**
 * Utility per inizializzare la directory e la struttura del database.
 * Questa classe crea la directory del database se non esiste e avvia Hibernate per la creazione delle tabelle.
 */
public class DatabaseInitializer {

    public static void main(String[] args) {
        // Assicura che la directory database esista
        initDatabaseDirectory();

        System.out.println("Inizializzazione database...");

        // Questa chiamata inizializza Hibernate e crea il database
        Session session = ConnectionDBUtils.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // Inizializza le tabelle degli esercizi con i dati predefiniti
//            DefaultExerciseInitializer.initializeDefaultExercises();
            tx.commit();
            System.out.println("Database inizializzato con successo!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            ConnectionDBUtils.closeSessionFactory();
        }
    }

    /**
     * Inizializza la directory del database se non esiste.
     * Crea la directory "database" nella root del progetto.
     */
    private static void initDatabaseDirectory() {
        File dbDir = new File("database");
        if (!dbDir.exists()) {
            boolean created = dbDir.mkdirs();
            if (!created) {
                System.err.println("Impossibile creare la directory del database");
            }
        }
    }
}