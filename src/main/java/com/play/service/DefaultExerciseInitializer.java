package com.play.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.play.dao.exercise.ExerciseDAO;
import com.play.dto.exercise.ExerciseDto;
import com.play.dto.exercise.JsonExerciseDto;
import com.play.factory.ExerciseFactory;
import com.play.model.entity.exercise.Exercise;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service che gestisce l'inizializzazione degli esercizi di default all'avvio dell'applicazione.
 * Carica gli esercizi da un file JSON e li salva nel database se non già presenti.
 */
public class DefaultExerciseInitializer {

    private static final Logger LOG = Logger.getLogger(DefaultExerciseInitializer.class.getName());
    private static final String EXERCISES_FILE = "/exercise/default_exercises.json";
    private static final ExerciseDAO EXERCISE_DAO = new ExerciseDAO();

    /**
     * Inizializza gli esercizi di default se non sono presenti nel database.
     */
    public static void initializeDefaultExercises() {
        LOG.info("Avvio controllo per l'inizializzazione degli esercizi di default");

        try (InputStream is = DefaultExerciseInitializer.class.getResourceAsStream(EXERCISES_FILE)) {
            if (is == null) {
                LOG.severe("File degli esercizi di default non trovato: " + EXERCISES_FILE);
                return;
            }

            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<JsonExerciseDto>>>() {}.getType();
            Map<String, List<JsonExerciseDto>> data = gson.fromJson(reader, type);

            List<JsonExerciseDto> jsonExercises = data.get("exercises");
            if (jsonExercises == null || jsonExercises.isEmpty()) {
                LOG.warning("Nessun esercizio trovato nel file JSON di default.");
                return;
            }

            int createdCount = 0;
            for (JsonExerciseDto jsonDto : jsonExercises) {
                // Controlla se un esercizio con questa CHIAVE (titleKey) esiste già nel DB.
                // Il campo 'title' dell'entità conterrà la chiave per gli esercizi di default.
                if (EXERCISE_DAO.findByTitle(jsonDto.titleKey) == null) {
                    createAndSaveExercise(jsonDto);
                    createdCount++;
                }
            }

            if (createdCount > 0) {
                LOG.info(createdCount + " nuovi esercizi di default sono stati creati.");
            } else {
                LOG.info("Tutti gli esercizi di default sono già presenti nel database.");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Errore critico durante l'inizializzazione degli esercizi di default", e);
        }
    }

    /**
     * Crea e salva un esercizio nel database
     * @param jsonDto Il DTO letto dal file JSON.
     */
    private static void createAndSaveExercise(JsonExerciseDto jsonDto) {
        try {
            // Converte il DTO JSON in DTO, senza tradurre le chiavi.
            ExerciseDto exerciseDto = jsonDto.toExerciseDto();
            Exercise exercise = ExerciseFactory.fromDto(exerciseDto);
            EXERCISE_DAO.save(exercise);
            LOG.info("Esercizio di default creato con chiave titolo: " + exercise.getInfoExercise().getTitle());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Errore durante la creazione dell'esercizio con chiave titolo: " + jsonDto.titleKey, e);
        }
    }
}