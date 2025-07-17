package com.play.service;

import com.play.dto.RankTable;
import com.play.dto.user.StudentDto;
import com.play.exceptions.ApplicationException;
import com.play.exceptions.code.user.UserServiceErrorCode;
import com.play.exceptions.service.user.UserServiceException;
import com.play.exceptions.validation.user.UserValidationException;
import com.play.factory.UserFactory;
import com.play.model.entity.user.Student;
import com.play.model.entity.user.User;
import com.play.util.validation.ErrorCollector;
import com.play.util.validation.UserValidation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.play.dao.user.StudentDAO;

/**
 * Service che gestisce le operazioni relative agli studenti dell'applicazione.
 * Permette la registrazione, il login, la validazione dei dati e la gestione delle classifiche.
 */
public final class StudentService {

    private static final Logger LOG = Logger.getLogger(StudentService.class.getName());
    private static final StudentDAO STUDENT_DAO = new StudentDAO();


    private StudentService() {
    }

    /**
     * Restituisce il numero totale di studenti registrati nell'applicazione.
     *
     * @return Il numero totale di studenti.
     */
    public static int getTotalStudentCount() {
        return STUDENT_DAO.countAll();
    }

    /**
     * Restituisce il ranking dello studente corrente in base al punteggio totale.
     *
     * @param currentUser Lo studente corrente per il quale calcolare il ranking.
     * @return La posizione dello studente nella classifica, o 0 se non trovato.
     */
    public static int getStudentRank(User currentUser) {
        List<Student> allStudents = STUDENT_DAO.findAll();

        Map<String, Integer> studentScores = allStudents.stream()
                .collect(Collectors.toMap(
                        Student::getUsername,
                        ScoreService::getTotalScoreForUser
                ));

        List<Map.Entry<String, Integer>> sortedStudents = studentScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < sortedStudents.size(); i++) {
            if (sortedStudents.get(i).getKey().equals(currentUser.getUsername())) {
                return i + 1;
            }
        }
        return 0;
    }
    public static boolean registerStudent(StudentDto studentDto) {
        return registerStudent(studentDto, null);
    }

    /**
     * Registra un nuovo studente nell'applicazione.
     * Esegue le validazioni sui dati dello studente e gestisce eventuali errori tramite ErrorCollector.
     *
     * @param studentDto I dati dello studente da registrare.
     * @param errorCollector L'oggetto per raccogliere gli errori di validazione.
     * @return true se la registrazione è avvenuta con successo, false altrimenti.
     */
    public static boolean registerStudent(StudentDto studentDto, ErrorCollector errorCollector) {
        if (errorCollector == null) {
            errorCollector = new ErrorCollector();
        }

        try {
            // Esegui tutte le validazioni raccogliendo eventuali errori
            validateStudentData(studentDto, errorCollector);

            // Se ci sono errori di validazione, interrompi la registrazione
            if (errorCollector.hasErrors()) {
                LOG.warning("Errori di validazione durante la registrazione");
                return false;
            }

            // Verifica che lo username non sia già in uso
            Student existingStudent = STUDENT_DAO.findByUsername(studentDto.username());
            if (existingStudent != null) {
                UserServiceException exception = new UserServiceException(UserServiceErrorCode.ALREADY_EXISTS);
                errorCollector.addError(exception);
                LOG.warning("Username già in uso");
                return false;
            }

            // Creazione dell'oggetto Student tramite Factory
            Student student = UserFactory.createStudent(studentDto);

            // Salvataggio nel database tramite DAO
            STUDENT_DAO.save(student);

            LOG.info("Registrazione studente completata con successo");
            return true;
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                errorCollector.addError((ApplicationException) e);
            }
            LOG.severe("Errore durante la registrazione dello studente");
            return false;
        }
    }

    public static boolean loginStudent(StudentDto studentDto) {
        return loginStudent(studentDto, null);
    }

    /**
     * Gestisce il login di uno studente nell'applicazione.
     * Verifica le credenziali e gestisce eventuali errori tramite ErrorCollector.
     *
     * @param studentDto I dati dello studente per il login.
     * @param errorCollector L'oggetto per raccogliere gli errori di validazione.
     * @return true se il login è avvenuto con successo, false altrimenti.
     */
    public static boolean loginStudent(StudentDto studentDto, ErrorCollector errorCollector) {
        if (errorCollector == null) {
            errorCollector = new ErrorCollector();
        }

        try {
            Student existingStudent = STUDENT_DAO.findByUsername(studentDto.username());
            if (existingStudent == null) {
                UserServiceException exception = new UserServiceException(UserServiceErrorCode.NOT_FOUND);
                errorCollector.addError(exception);
                LOG.warning("Studente non trovato");
                return false;
            }

            if(!existingStudent.getPassword().equals(studentDto.password())) {
                UserServiceException exception = new UserServiceException(UserServiceErrorCode.INVALID_PASSWORD);
                errorCollector.addError(exception);
                LOG.warning("Credenziali non valide per lo studente");
                return false;
            }

            LOG.info("Login studente effettuato con successo" );
            return true;

        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                errorCollector.addError((ApplicationException) e);
            }
            LOG.severe("Errore durante la registrazione dello studente");
            return false;
        }
    }

    /**
     * Restituisce la classifica degli studenti ordinata per punteggio totale.
     * Ogni voce della classifica contiene la posizione, lo studente e il punteggio.
     *
     * @return Una lista di RankTable contenente le informazioni sulla classifica degli studenti.
     */
    public static List<RankTable> getStudentRankings() {
        List<Student> allStudents = STUDENT_DAO.findAll();

        // Crea una lista di voci con studente e punteggio, ordinate per punteggio
        List<RankTable> sortedEntries = allStudents.stream()
                .map(student -> new RankTable(0, student, ScoreService.getTotalScoreForUser(student)))
                .sorted(Comparator.comparingInt(RankTable::score).reversed())
                .collect(Collectors.toList());

        // Assegna la posizione in classifica
        List<RankTable> rankedEntries = new ArrayList<>();
        for (int i = 0; i < sortedEntries.size(); i++) {
            RankTable current = sortedEntries.get(i);
            rankedEntries.add(new RankTable(i + 1, current.student(), current.score()));
        }

        return rankedEntries;
    }

    /**
     * Esegue le validazioni sui dati dello studente e raccoglie eventuali errori.
     * Utilizza UserValidation per verificare username, password, nome e cognome.
     *
     * @param studentDto I dati dello studente da validare.
     * @param errorCollector L'oggetto per raccogliere gli errori di validazione.
     */
    private static void validateStudentData(StudentDto studentDto, ErrorCollector errorCollector) {
        try {
            UserValidation.validateUsername(studentDto.username());
        } catch (UserValidationException e) {
            errorCollector.addError(e);
        }

        try {
            UserValidation.validatePassword(studentDto.password());
        } catch (UserValidationException e) {
            errorCollector.addError(e);
        }

        try {
            UserValidation.validateFirstName(studentDto.firstName());
        } catch (UserValidationException e) {
            errorCollector.addError(e);
        }

        try {
            UserValidation.validateLastName(studentDto.lastName());
        } catch (UserValidationException e) {
            errorCollector.addError(e);
        }
    }
}





