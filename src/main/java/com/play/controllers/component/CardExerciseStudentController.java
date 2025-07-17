package com.play.controllers.component;

import com.play.controllers.util.IconUtils;
import com.play.controllers.util.SceneLoader;
import com.play.controllers.util.SessionManager;
import com.play.controllers.view.GameExerciseController;
import com.play.dto.ScoreDto;
import com.play.dto.exercise.ExerciseDto;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.user.User;
import com.play.model.enums.Language;
import com.play.service.ExerciseService;
import com.play.service.ScoreService;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller per il componente FXML che rappresenta la card di un esercizio nella vista studente.
 * Mostra le informazioni dell'esercizio, il punteggio dell'utente e un pulsante per avviare il gioco.
 */
public class CardExerciseStudentController implements Initializable {

    @FXML
    private Label category;

    @FXML
    private TextArea description;

    @FXML
    private Label difficulty;

    @FXML
    private FontIcon iconExercise;

    @FXML
    private MFXButton playBtn;

    @FXML
    private Label scoreText;

    @FXML
    private Label scoreTotal;

    @FXML
    private Label scoreUser;

    @FXML
    private Label title;

    @FXML
    private Label topic;

    @FXML
    private HBox containerScore;

    /** DTO contenente i dati dell'esercizio da visualizzare. */
    private ExerciseDto exerciseDto;

    private static final Logger LOG = Logger.getLogger(CardExerciseStudentController.class.getName());

    private static final String DIFFICULTY = "difficulty";
    private static final String TOPIC = "topic";
    private static final String CATEGORY = "category";

    private static final String SUBFOLDER_UI = "exercise";
    private static final String SCORE_TEXT = "scoreTextCard";
    private static final String PLAY_BTN = "playBtn";
    private static final String SUBFOLDER_DEFAULT_EXERCISE = "exercise";


    /**
     * Inizializza il controller.
     * Imposta i listener per l'aggiornamento della lingua e l'azione del pulsante "Gioca".
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateText(SessionManager.getInstance().getCurrentLanguage());
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));

        playBtn.setOnAction(this::handlePlayAction);

    }

    /**
     * Gestisce l'azione di click sul pulsante "Gioca".
     * Carica la scena del gioco e passa l'esercizio selezionato al suo controller.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handlePlayAction(Event event) {
        if (exerciseDto == null) {
            LOG.severe("Impossibile avviare l'esercizio.");
            return;
        }

        Exercise exercise = ExerciseService.findExerciseByTitle(exerciseDto.infoExercise().title());
        if (exercise == null) {
            LOG.severe("Esercizio non trovato.");
            return;
        }

        final String PATH = "GameExercise.fxml";
        GameExerciseController controller = SceneLoader.loadSceneAndGetController(PATH, playBtn);

        if (controller != null) {
            controller.loadExercise(exercise);
        } else {
            LOG.severe("Impossibile caricare il controller per GameExercise.fxml");
        }
    }


    /**
     * Imposta i dati dell'esercizio per questa card e aggiorna la UI, incluso il punteggio.
     *
     * @param exerciseDto Il DTO dell'esercizio da visualizzare.
     */
    public void setCardData(ExerciseDto exerciseDto) {
        this.exerciseDto = exerciseDto;
        iconExercise.setIconCode(IconUtils.getIconForCategory(exerciseDto.infoExercise().category()));
        updateText(SessionManager.getInstance().getCurrentLanguage());
        updateScore();
    }

    /**
     * Aggiorna la sezione del punteggio recuperando i dati dal servizio.
     * Mostra il punteggio dell'utente e quello totale per l'esercizio.
     */
    private void updateScore() {
        if (exerciseDto == null) {
            return;
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();
        Exercise exercise = ExerciseService.findExerciseByTitle(exerciseDto.infoExercise().title());
        if (exercise == null) {
            scoreUser.setText("0");
            scoreTotal.setText("N/A");
            return;
        }

        ScoreDto scoreDto = ScoreService.getScoreForUserAndExercise(currentUser, exercise);
        scoreUser.setText(String.valueOf(scoreDto.userScore()));
        scoreTotal.setText(String.valueOf(scoreDto.totalScore()));
    }

    /**
     * Nasconde il pulsante "Gioca" e la sezione del punteggio.
     * Utile per la visualizzazione in contesti di anteprima.
     */
    public void hidePlayAndScore() {
        if (playBtn != null) {
            playBtn.setVisible(false);
            playBtn.setManaged(false);
        }
        if (containerScore != null) {
            containerScore.setVisible(false);
            containerScore.setManaged(false);
        }
    }

    /**
     * Aggiorna i testi della UI in base alla lingua selezionata.
     *
     * @param lang La lingua da utilizzare per la traduzione.
     */
    private void updateText(Language lang) {
        Locale locale = lang.getLocale();
        scoreText.setText(I18NUtils.getUI(SUBFOLDER_UI, SCORE_TEXT, locale));
        playBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, PLAY_BTN, locale));

        if (exerciseDto != null) {
            title.setText(I18NUtils.translateIfKey(SUBFOLDER_DEFAULT_EXERCISE, exerciseDto.infoExercise().title(), locale));
            description.setText(I18NUtils.translateIfKey(SUBFOLDER_DEFAULT_EXERCISE, exerciseDto.infoExercise().description(), locale));

            category.setText(I18NUtils.getEnum(CATEGORY, exerciseDto.infoExercise().category().textUIKey(), locale));
            topic.setText(I18NUtils.getEnum(TOPIC, exerciseDto.infoExercise().topic().getTextUIKeyy(), locale));
            difficulty.setText(I18NUtils.getEnum(DIFFICULTY, exerciseDto.difficulty().getTextUIKey(), locale));
        }
    }
}

