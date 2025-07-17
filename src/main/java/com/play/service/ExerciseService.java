package com.play.service;

import com.play.dao.exercise.ExerciseDAO;
import com.play.dto.exercise.AnswerDto;
import com.play.dto.exercise.ExerciseDto;
import com.play.dto.exercise.InfoExerciseDto;
import com.play.dto.exercise.QuestionDto;
import com.play.exceptions.ApplicationException;
import com.play.exceptions.code.exercise.ExerciseServiceErrorCode;
import com.play.exceptions.code.exercise.QuestionValidateErrorCode;
import com.play.exceptions.service.exercise.ExerciseServiceException;
import com.play.exceptions.validation.exercise.ExerciseValidationException;

import com.play.exceptions.validation.exercise.InfoExerciseValidationException;
import com.play.exceptions.validation.exercise.QuestionValidationException;
import com.play.factory.ExerciseFactory;
import com.play.model.entity.exercise.Exercise;
import com.play.model.enums.Difficulty;
import com.play.util.validation.*;
import org.hibernate.HibernateException;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service che gestisce le operazioni sugli esercizi, come creazione, validazione, ricerca e cancellazione.
 * Fornisce metodi statici per interagire con il DAO degli esercizi.
 */
public final  class ExerciseService {

    private static final Logger LOG = Logger.getLogger(ExerciseService.class.getName());
    private static final ExerciseDAO EXERCISE_DAO = new ExerciseDAO();

    private ExerciseService() {
    }

    /**
     * Crea un nuovo esercizio a partire da un DTO.
     * @param exerciseDto Il DTO contenente i dati dell'esercizio da creare.
     * @return L'istanza di Exercise creata.
     * @throws ExerciseServiceException Se si verifica un errore durante la creazione dell'esercizio.
     */
    public static Exercise createExercise(ExerciseDto exerciseDto) throws ExerciseServiceException {

        Exercise exercise = ExerciseFactory.fromDto(exerciseDto);

        try {
            EXERCISE_DAO.save(exercise);
            LOG.info("Esercizio creato con successo");
            return exercise;
        } catch (ApplicationException e) {
            LOG.severe("Errore durante la creazione dell'esercizio: " + e.getMessage());
            throw new ExerciseServiceException(ExerciseServiceErrorCode.EXERCISE_SAVE_FAILED, e);
        }

    }

    public static boolean validateInfoExerciseStep(InfoExerciseDto infoExerciseDto, Difficulty difficulty) {
        return ExerciseService.validateInfoExerciseStep(infoExerciseDto, difficulty);
    }

    /**
     * Valida i dati di un esercizio informativo.
     * @param infoExerciseDto Il DTO contenente i dati dell'esercizio informativo da validare.
     * @param difficulty La difficoltà dell'esercizio.
     * @param errorCollector Un oggetto per raccogliere eventuali errori di validazione.
     * @return true se i dati sono validi e non esiste già un esercizio con lo stesso titolo, false altrimenti.
     */
    public static boolean validateInfoExerciseStep(InfoExerciseDto infoExerciseDto, Difficulty difficulty, ErrorCollector errorCollector) {
        if (errorCollector == null) {
            errorCollector = new ErrorCollector();
        }
        try {
            validateInfoExerciseData(infoExerciseDto, difficulty, errorCollector);

            if (!errorCollector.hasErrors()) {
                Exercise existingExercise = EXERCISE_DAO.findByTitle(infoExerciseDto.title().toLowerCase());
                if (existingExercise != null) {
                    errorCollector.addError(new ExerciseServiceException(ExerciseServiceErrorCode.ALREADY_TITLE_EXISTS));
                }
            }
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                errorCollector.addError((ApplicationException) e);
            }
            return false;
        }
        return !errorCollector.hasErrors();
    }


    public static boolean validateSingleQuestion(QuestionDto questionDto) {
        return validateSingleQuestion(questionDto, null);
    }

    /**
     * Valida una singola domanda.
     * @param questionDto Il DTO contenente i dati della domanda da validare.
     * @return true se la domanda è valida, false altrimenti.
     */
    public static boolean validateSingleQuestion(QuestionDto questionDto, ErrorCollector errorCollector) {
        if (errorCollector == null) {
            errorCollector = new ErrorCollector();
        }

        try {
            ValidationQuestion.validateQuestionText(questionDto.questionText());
        } catch (ApplicationException e) {
            errorCollector.addError(QuestionValidationField.QUESTION_TEXT, e);
        }

        boolean onCorrectAnswers = false;
        List<AnswerDto> answers = questionDto.answers();
        QuestionValidationField[] answerKeys = {
                QuestionValidationField.ANSWER_A, QuestionValidationField.ANSWER_B,
                QuestionValidationField.ANSWER_C, QuestionValidationField.ANSWER_D,
        };

        if (answers != null) {
            for (int i = 0; i < answers.size(); i++) {
                if (i>= answerKeys.length) break;
                AnswerDto answerDto = answers.get(i);
                if (answerDto.isCorrect()) {
                    onCorrectAnswers = true;
                }
                try {
                    ValidationAnswer.validateText(answerDto.text());
                } catch (ApplicationException e) {
                    errorCollector.addError(answerKeys[i], e);
                }
            }
        }
        if (!onCorrectAnswers) {
            errorCollector.addError(QuestionValidationField.NO_CORRECT_ANSWER_SELECTED, new QuestionValidationException(QuestionValidateErrorCode.NO_CORRECT_ANSWER_SELECTED));
        }

        return errorCollector.hasErrors();
    }

    /**
     * Valida i dati di un esercizio informativo.
     * @param infoExerciseDto Il DTO contenente i dati dell'esercizio informativo da validare.
     * @param difficulty La difficoltà dell'esercizio.
     * @param errorCollector Un oggetto per raccogliere eventuali errori di validazione.
     */
    private static void validateInfoExerciseData(InfoExerciseDto infoExerciseDto, Difficulty difficulty,  ErrorCollector errorCollector) {

        try {
            ValidationInfoExercise.validateTitle(infoExerciseDto.title());
        } catch (InfoExerciseValidationException e) {
            errorCollector.addError(e);
        }

        try {
            ValidationInfoExercise.validateDescription(infoExerciseDto.description());
        } catch (InfoExerciseValidationException e) {
            errorCollector.addError(e);
        }

        try {
            ValidationInfoExercise.validateCategory(infoExerciseDto.category());
        } catch (InfoExerciseValidationException e) {
            errorCollector.addError(e);
        }

        try {
            ValidationInfoExercise.validateTopic(infoExerciseDto.topic());
        } catch (InfoExerciseValidationException e) {
            errorCollector.addError(e);
        }

        try {
            ValidationExercise.validateDifficulty(difficulty);
        } catch (ExerciseValidationException e) {
            errorCollector.addError(e);
        }
    }


    /**
     * Recupera un esercizio completo tramite il suo titolo.
     * @param title Il titolo dell'esercizio da recuperare.
     * @return Un DTO contenente i dati dell'esercizio, o null se non trovato.
     */
    public static List<ExerciseDto> getAllExercises() {
        List<Exercise> exercises = EXERCISE_DAO.findAll();
        return exercises.stream()
                .map(ExerciseFactory::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Trova un esercizio completo tramite il suo titolo.
     * @param title Il titolo dell'esercizio.
     * @return L'entità Exercise completa, o null se non trovata.
     */
    public static Exercise findExerciseByTitle(String title) {
        try {
            return EXERCISE_DAO.findByTitle(title);
        } catch (HibernateException e) {
            LOG.severe("Errore durante la ricerca dell'esercizio per titolo: " + title);
            return null;
        }
    }


    /**
     * Elimina un esercizio tramite il suo titolo.
     * @param title Il titolo dell'esercizio da eliminare.
     * @param errorCollector Un oggetto per raccogliere eventuali errori di validazione.
     */
    public static void deleteExerciseByTitle(String title, ErrorCollector errorCollector) {
        try {
            Exercise exercise = EXERCISE_DAO.findByTitle(title);
            if (exercise == null) {
                errorCollector.addError( new ExerciseServiceException(ExerciseServiceErrorCode.EXERCISE_NOT_FOUND));
            }
            EXERCISE_DAO.delete(exercise);
            LOG.info("Esercizio con titolo eliminato con successo.");
        } catch (ApplicationException e) {
            LOG.severe("Errore durante l'eliminazione dell'esercizio con titolo");
            errorCollector.addError( new ExerciseServiceException(ExerciseServiceErrorCode.EXERCISE_DELETE_FAILED));
        }
    }


}
