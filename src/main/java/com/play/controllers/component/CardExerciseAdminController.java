package com.play.controllers.component;

import com.play.controllers.util.IconUtils;
import com.play.controllers.util.SessionManager;
import com.play.controllers.view.ExerciseAdminController;
import com.play.dto.exercise.ExerciseDto;
import com.play.model.enums.Language;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller per il componente FXML che rappresenta la card di un esercizio nella vista amministratore.
 * Mostra le informazioni dell'esercizio e fornisce un pulsante per avviare il processo di cancellazione.
 */
public class CardExerciseAdminController implements Initializable {

    @FXML
    private Label category;

    @FXML
    private MFXButton deleteBtn;

    @FXML
    private TextArea description;

    @FXML
    private Label difficulty;

    @FXML
    private FontIcon iconExercise;

    @FXML
    private Label title;

    @FXML
    private Label topic;

    /** DTO contenente i dati dell'esercizio da visualizzare. */
    private ExerciseDto exerciseDto;
    /** Riferimento al controller della vista principale per gestire la cancellazione. */
    private ExerciseAdminController exerciseAdminController;
    /** Riferimento al nodo radice della card per poterlo rimuovere dalla UI. */
    private Node cardNode;

    private static final String DIFFICULTY = "difficulty";
    private static final String TOPIC = "topic";
    private static final String CATEGORY = "category";
    private static final String SUBFOLDER_DEFAULT_EXERCISE = "exercise";

    private static final Logger LOG = Logger.getLogger(CardExerciseAdminController.class.getName());

    /**
     * Inizializza il controller.
     * Imposta i listener per l'aggiornamento della lingua e l'azione del pulsante di cancellazione.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateText(SessionManager.getInstance().getCurrentLanguage());
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));

        deleteBtn.setOnAction(event -> handleDeleteExercise());
    }

    /**
     * Imposta i dati dell'esercizio per questa card e aggiorna la UI.
     *
     * @param exerciseDto Il DTO dell'esercizio da visualizzare.
     */
    public void setCardData(ExerciseDto exerciseDto) {
        this.exerciseDto = exerciseDto;
        iconExercise.setIconCode(IconUtils.getIconForCategory(exerciseDto.infoExercise().category()));
        updateText(SessionManager.getInstance().getCurrentLanguage());
    }

    /**
     * Imposta il riferimento al controller della vista principale degli esercizi.
     *
     * @param exerciseAdminController Il controller della vista genitore.
     */
    public void setExerciseAdminController(ExerciseAdminController exerciseAdminController) {
        this.exerciseAdminController = exerciseAdminController;
    }

    /**
     * Imposta il riferimento al nodo UI di questa card.
     *
     * @param cardNode Il nodo radice del componente card.
     */
    public void setCardNode(Node cardNode) {
        this.cardNode = cardNode;
    }

    /**
     * Gestisce l'azione di click sul pulsante di cancellazione.
     * Chiama il metodo del controller genitore per mostrare il dialogo di conferma.
     */
    private void handleDeleteExercise() {
        if (exerciseDto != null && exerciseAdminController != null && cardNode != null) {
            exerciseAdminController.showDeleteConfirmationDialog(exerciseDto, cardNode);
        }
    }

    /**
     * Aggiorna i testi della UI in base alla lingua selezionata.
     *
     * @param lang La lingua da utilizzare per la traduzione.
     */
    private void updateText(Language lang) {
        Locale locale = lang.getLocale();

        if (exerciseDto != null) {
            title.setText(I18NUtils.translateIfKey(SUBFOLDER_DEFAULT_EXERCISE, exerciseDto.infoExercise().title(), locale));
            description.setText(I18NUtils.translateIfKey(SUBFOLDER_DEFAULT_EXERCISE, exerciseDto.infoExercise().description(), locale));

            category.setText(I18NUtils.getEnum(CATEGORY, exerciseDto.infoExercise().category().textUIKey(), locale));
            topic.setText(I18NUtils.getEnum(TOPIC, exerciseDto.infoExercise().topic().getTextUIKeyy(), locale));
            difficulty.setText(I18NUtils.getEnum(DIFFICULTY, exerciseDto.difficulty().getTextUIKey(), locale));
        }
    }
}
