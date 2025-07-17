package com.play.controllers.view;

import com.play.controllers.component.CardExerciseAdminController;
import com.play.controllers.component.NotificationController;
import com.play.controllers.component.SidebarAdminController;
import com.play.controllers.util.NotificationType;
import com.play.controllers.util.SessionManager;
import com.play.dto.exercise.ExerciseDto;
import com.play.exceptions.ApplicationException;
import com.play.model.enums.Category;
import com.play.model.enums.Language;
import com.play.service.ExerciseService;
import com.play.util.provider.I18NUtils;
import com.play.util.validation.ErrorCollector;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.play.exceptions.code.exercise.ExerciseServiceErrorCode.EXERCISE_DELETE_FAILED;
import static com.play.exceptions.code.exercise.ExerciseServiceErrorCode.EXERCISE_NOT_FOUND;

/**
 * Controller per la gestione della vista degli esercizi lato amministratore.
 * Consente di filtrare per categoria, visualizzare le card e cancellare esercizi.
 */
public class ExerciseAdminController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private SidebarAdminController sidebarAdminController;

    @FXML
    private Label titlePage;

    @FXML
    private Label filter;

    @FXML
    private MFXButton allCategoriesBtn;

    @FXML
    private MFXButton category1Btn;

    @FXML
    private MFXButton category2Btn;

    @FXML
    private MFXButton category3Btn;

    @FXML
    private FlowPane cardContainer;

    @FXML
    private HBox conteinerNotFound;

    @FXML
    private Label notFoundExercise;

    @FXML
    private VBox dialogDeleteExercise;

    @FXML
    private Label dialogTitle;

    @FXML
    private Label messageDialog;

    @FXML
    private MFXButton cancelDialogBtn;

    @FXML
    private MFXButton confirmDialogBtn;

    /** Lista di tutti gli esercizi disponibili. */
    private List<ExerciseDto> allExercises;
    /** Pulsante attualmente selezionato per il filtro categoria. */
    private MFXButton selectedButton;
    /** Esercizio selezionato per la cancellazione. */
    private ExerciseDto exerciseToDelete;
    /** Nodo della card da rimuovere dopo la cancellazione. */
    private Node cardToDelete;

    private static final String SELECTED_STYLE_CLASS = "selected";
    private static final String CATEGORY = "category";
    private static final String FIND_ERROR = "find_error";
    private static final String OUTPUT = "output";
    private static final String SOLVE_CODE = "solve_code";
    private static final String ALL = "btnAll";
    private static final String FILTER_TEXT = "filter";
    private static final String SUBFOLDER_UI = "exercise";
    private static final String TITLE_PAGE = "titlePage";
    private static final String NOT_FOUND_EXERCISE = "exerciseNotFound";
    private static final String DIALOG_TITLE_KEY = "titleDialog";
    private static final String DIALOG_MESSAGE_KEY = "messsageDialog";
    private static final String DIALOG_CANCEL_KEY = "btnDialogCancel";
    private static final String DIALOG_CONFIRM_KEY = "btnDialogDelete";
    private static final String SUCCESS_DELETE_TITLE = "notfySuccessTitle";
    private static final String SUCCESS_DELETE_MESSAGE = "notfySuccessMessage";

    private static final Logger LOG = Logger.getLogger(ExerciseAdminController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidebarAdminController.selectExerciseButton();
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());

        loadAllExercises();
        setupFilterButtons();
        selectButton(allCategoriesBtn);

        cancelDialogBtn.setOnAction(event -> hideDeleteConfirmationDialog());
        confirmDialogBtn.setOnAction(event -> confirmDeleteExercise());
        dialogDeleteExercise.setVisible(false);
    }

    /**
     * Carica tutti gli esercizi disponibili dal servizio e li visualizza.
     */
    private void loadAllExercises() {
        this.allExercises = ExerciseService.getAllExercises();
        displayExercises(allExercises);
    }

    /**
     * Configura i pulsanti per il filtro delle categorie.
     * Ogni pulsante filtra gli esercizi per una specifica categoria.
     */
    private void setupFilterButtons() {
        allCategoriesBtn.setOnAction(event -> {
            displayExercises(allExercises);
            selectButton(allCategoriesBtn);
        });
        category1Btn.setOnAction(event -> {
            filterExercisesByCategory(Category.FIND_ERROR);
            selectButton(category1Btn);
        });
        category2Btn.setOnAction(event -> {
            filterExercisesByCategory(Category.OUTPUT);
            selectButton(category2Btn);
        });
        category3Btn.setOnAction(event -> {
            filterExercisesByCategory(Category.SOLVE_CODE);
            selectButton(category3Btn);
        });
    }

    /**
     * Filtra gli esercizi per la categoria specificata e li visualizza.
     *
     * @param category Categoria su cui filtrare gli esercizi.
     */
    private void filterExercisesByCategory(Category category) {
        List<ExerciseDto> filteredExercises = allExercises.stream()
                .filter(exercise -> exercise.infoExercise().category().equals(category))
                .collect(Collectors.toList());
        displayExercises(filteredExercises);
    }

    /**
     * Visualizza le card degli esercizi nella UI.
     * Se la lista è vuota, mostra il messaggio "nessun esercizio trovato".
     *
     * @param exercises Lista di esercizi da visualizzare.
     */
    private void displayExercises(List<ExerciseDto> exercises) {
        cardContainer.getChildren().clear();

        if (exercises == null || exercises.isEmpty()) {
            showNotFoundExercise();
        } else {
            hideNotFoundExercise();
            for (ExerciseDto exercise : exercises) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CardExerciseAdmin.fxml"));
                    Node cardNode = loader.load();
                    CardExerciseAdminController controller = loader.getController();
                    controller.setCardData(exercise);
                    controller.setExerciseAdminController(this);
                    controller.setCardNode(cardNode);
                    cardContainer.getChildren().add(cardNode);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Impossibile caricare la card dell'esercizio", e);
                }
            }
        }
    }

    public void showDeleteConfirmationDialog(ExerciseDto exerciseDto, Node cardNode) {
        this.exerciseToDelete = exerciseDto;
        this.cardToDelete = cardNode;
        dialogDeleteExercise.setVisible(true);
    }

    private void hideDeleteConfirmationDialog() {
        dialogDeleteExercise.setVisible(false);
        this.exerciseToDelete = null;
        this.cardToDelete = null;
    }

    /**
     * Conferma la cancellazione dell'esercizio selezionato.
     * Mostra una notifica di successo o errore e aggiorna la UI.
     */
    private void confirmDeleteExercise() {
        if (exerciseToDelete == null) {
            return;
        }
        ErrorCollector errorCollector = new ErrorCollector();
        ExerciseService.deleteExerciseByTitle(exerciseToDelete.infoExercise().title(), errorCollector);

        if (!errorCollector.hasErrors()) {
            String title = I18NUtils.getUI(SUBFOLDER_UI, SUCCESS_DELETE_TITLE, SessionManager.getInstance().getCurrentLanguage().getLocale());
            String message = I18NUtils.getUI(SUBFOLDER_UI, SUCCESS_DELETE_MESSAGE, SessionManager.getInstance().getCurrentLanguage().getLocale());
            showNotification(NotificationType.SUCCESS, title, message);

            cardContainer.getChildren().remove(cardToDelete);
            if (cardContainer.getChildren().isEmpty()) {
                showNotFoundExercise();
            }
        } else {
            handleDeleteErrors(errorCollector);
        }
        hideDeleteConfirmationDialog();
    }

    /**
     * Gestisce gli errori di cancellazione mostrando notifiche specifiche.
     *
     * @param errorCollector Collezione di errori generati dal servizio.
     */
    private void handleDeleteErrors(ErrorCollector errorCollector) {
        for (ApplicationException e : errorCollector.getErrors()) {
            String titleKey;
            String messageKey;
            NotificationType type;

            switch (e.getErrorCode()) {
                case EXERCISE_DELETE_FAILED:
                    titleKey = EXERCISE_DELETE_FAILED.getTitleKey();
                    messageKey = EXERCISE_DELETE_FAILED.getMessageKey();
                    type = NotificationType.ERROR;
                    break;
                case EXERCISE_NOT_FOUND:
                    titleKey = EXERCISE_NOT_FOUND.getTitleKey();
                    messageKey = EXERCISE_NOT_FOUND.getMessageKey();
                    type = NotificationType.WARNING;
                    break;
                default:
                    continue;
            }
            String notificationTitle = I18NUtils.getError(SUBFOLDER_UI, titleKey, SessionManager.getInstance().getCurrentLanguage().getLocale());
            String notificationMessage = I18NUtils.getError(SUBFOLDER_UI, messageKey, SessionManager.getInstance().getCurrentLanguage().getLocale());
            showNotification(type, notificationTitle, notificationMessage);
        }
    }

    /**
     * Mostra una notifica a schermo.
     *
     * @param type    Tipo di notifica (SUCCESS, ERROR, WARNING).
     * @param title   Titolo della notifica.
     * @param message Messaggio della notifica.
     */
    private void showNotification(NotificationType type, String title, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Notification.fxml"));
            Node notificationNode = loader.load();
            NotificationController controller = loader.getController();

            controller.setIcon(type);
            controller.setTitle(title);
            controller.setMessage(message);

            StackPane notificationPane = new StackPane(notificationNode);
            notificationPane.setPickOnBounds(false);
            notificationPane.setStyle("-fx-padding: 20; -fx-alignment: TOP_RIGHT;");

            rootPane.getChildren().add(notificationPane);

            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(event -> rootPane.getChildren().remove(notificationPane));
            delay.play();

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Impossibile caricare la notifica", e);
        }
    }

    private void selectButton(MFXButton button) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove(SELECTED_STYLE_CLASS);
        }
        button.getStyleClass().add(SELECTED_STYLE_CLASS);
        selectedButton = button;
    }

    private void updateText(Language lang) {
        titlePage.setText(I18NUtils.getUI(SUBFOLDER_UI, TITLE_PAGE, lang.getLocale()));
        filter.setText(I18NUtils.getUI(SUBFOLDER_UI, FILTER_TEXT, lang.getLocale()));
        allCategoriesBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, ALL, lang.getLocale()));
        category1Btn.setText(I18NUtils.getEnum(CATEGORY, FIND_ERROR, lang.getLocale()));
        category2Btn.setText(I18NUtils.getEnum(CATEGORY, OUTPUT, lang.getLocale()));
        category3Btn.setText(I18NUtils.getEnum(CATEGORY, SOLVE_CODE, lang.getLocale()));
        notFoundExercise.setText(I18NUtils.getUI(SUBFOLDER_UI, NOT_FOUND_EXERCISE, lang.getLocale()));
        dialogTitle.setText(I18NUtils.getUI(SUBFOLDER_UI, DIALOG_TITLE_KEY, lang.getLocale()));
        messageDialog.setText(I18NUtils.getUI(SUBFOLDER_UI, DIALOG_MESSAGE_KEY, lang.getLocale()));
        cancelDialogBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, DIALOG_CANCEL_KEY, lang.getLocale()));
        confirmDialogBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, DIALOG_CONFIRM_KEY, lang.getLocale()));
    }

    private void showNotFoundExercise() {
        conteinerNotFound.setVisible(true);
        conteinerNotFound.setManaged(true);
    }

    private void hideNotFoundExercise() {
        conteinerNotFound.setVisible(false);
        conteinerNotFound.setManaged(false);
    }
}
