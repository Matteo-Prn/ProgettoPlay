package com.play.controllers.view;

import com.play.controllers.component.ForumQuestionController;
import com.play.controllers.component.NotificationController;
import com.play.controllers.component.RecapExerciseController;
import com.play.controllers.component.SidebarAdminController;
import com.play.controllers.util.NotificationType;
import com.play.controllers.util.SessionManager;
import com.play.dto.exercise.ExerciseDto;
import com.play.dto.exercise.InfoExerciseDto;
import com.play.dto.exercise.QuestionDto;
import com.play.exceptions.ApplicationException;
import com.play.exceptions.service.exercise.ExerciseServiceException;
import com.play.model.enums.Category;
import com.play.model.enums.Difficulty;
import com.play.model.enums.Language;
import com.play.model.enums.Topic;
import com.play.service.ExerciseService;
import com.play.util.provider.ConfigurationUtils;
import com.play.util.provider.I18NUtils;
import com.play.util.validation.ErrorCollector;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.play.exceptions.code.exercise.ExerciseServiceErrorCode.ALREADY_TITLE_EXISTS;
import static com.play.exceptions.code.exercise.ExerciseValidationErrorCode.EXERCISE_DIFFICULTY_EMPTY;
import static com.play.exceptions.code.exercise.ValidateInfoExerciseErrorCode.*;

/**
 * Controller per la vista di creazione di un nuovo esercizio.
 * Gestisce un wizard a più passaggi per inserire le informazioni generali,
 * le domande e infine riepilogare e creare l'esercizio.
 */
public class CreateExerciseController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private SidebarAdminController sidebarAdminController;

    @FXML
    private Label titlePage;

    @FXML
    private MFXButton backBtn;

    @FXML
    private MFXButton nextBtn;

    @FXML
    private MFXButton createBtn;

    @FXML
    private VBox containerForumInfoExercise;

    @FXML
    private VBox containerForumQuestion;

    @FXML
    private VBox containerRecapExercise;

    @FXML
    private Label titleForum;

    @FXML
    private Label floatingTitle;

    @FXML
    private TextField fieldTitle;

    @FXML
    private Label errorTitle;

    @FXML
    private Label floatingDifficulty;

    @FXML
    private MFXComboBox<Difficulty> fieldDifficulty;

    @FXML
    private Label errorDifficulty;

    @FXML
    private Label floatingDescription;

    @FXML
    private TextArea fieldDescription;

    @FXML
    private Label errorDescription;

    @FXML
    private Label floatingTopic;

    @FXML
    private MFXComboBox<Topic> fieldTopic;

    @FXML
    private Label errorTopic;

    @FXML
    private Label floatingCategory;

    @FXML
    private MFXComboBox<Category> fieldCategory;

    @FXML
    private Label errorCategory;

    @FXML
    private FontIcon iconPage;

    @FXML
    private ForumQuestionController forumQuestionController;

    @FXML
    private RecapExerciseController recapExerciseController;

    private static final String FILE_CONFIGURATION = "exerciseValidation.properties";
    private static final String PATH_TOTAL_QUESTION = "question.maxNumber";

    private static final String SUBFOLDER_UI = "create_exercise";
    private static final String SUBFOLDER_ERROR = "exercise";

    private static final String FORUM_TITLE = "forumTitle";
    private static final String QUESTION_TITLE = "questionTitle";
    private static final String RECAP_TITLE = "recapTitle";
    private static final String FORUM_SUBTITLE = "forumSubtitle";
    private static final String FLOATING_TEXT_TITLE = "floatingTextTitle";
    private static final String PROMPT_TITLE = "promptTitle";
    private static final String DIFFICULTY = "difficulty";
    private static final String PROMPT_DIFFICULTY = "promptDifficulty";
    private static final String FLOATING_DESCRIPTION = "floatingDescription";
    private static final String PROMPT_DESCRIPTION = "promptDescription";
    private static final String TOPIC = "topic";
    private static final String PROMPT_TOPIC = "promptTopic";
    private static final String CATEGORY = "category";
    private static final String PROMPT_CATEGORY = "promptCategory";
    private static final String BACK_BUTTON = "backButton";
    private static final String NEXT_BUTTON = "nextButton";
    private static final String CREATE_BUTTON = "createButton";

    private static final String ERROR_CREATE_TITLE = "error.exercise.save.failed.titleKey";
    private static final String ERROR_CREATE_MESSAGE = "error.exercise.save.failed.messageKey";

    private static final String NOTIFY_TITLE_SUCC = "notificationSuccessTitle";
    private static final String NOTIFY_MESSAGE_SUCC = "notificationSuccessMessage";


    /**
     * Indice dello step corrente nel processo di creazione.
     * 0: Info esercizio, 1-N: Domande, N+1: Riepilogo.
     */
    private int currentStep = 0;
    private static final int TOTAL_QUESTION = ConfigurationUtils.getInt(FILE_CONFIGURATION, PATH_TOTAL_QUESTION);
    /**
     * Lista dei DTO delle domande per l'esercizio in creazione.
     */
    private final List<QuestionDto> questions = new ArrayList<>(Collections.nCopies(TOTAL_QUESTION, null));
    /**
     * DTO contenente le informazioni generali dell'esercizio.
     */
    private InfoExerciseDto infoExerciseDto;

    private static final Logger LOG = Logger.getLogger(CreateExerciseController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hidePaneCreateQuestion();
        hidePaneRecapExercise();
        hideErrors();
        updateStepState();
        sidebarAdminController.selectCreateButton();
        updateText(SessionManager.getInstance().getCurrentLanguage());
        populateComboBoxes();
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateText(newValue);
                });

        fieldTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            resetErrorsInfoExercise();
        });

        fieldDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            resetErrorsInfoExercise();
        });

        fieldDifficulty.textProperty().addListener((observable, oldValue, newValue) -> {
            resetErrorsInfoExercise();
        });

        fieldTopic.textProperty().addListener((observable, oldValue, newValue) -> {
            resetErrorsInfoExercise();
        });

        fieldCategory.textProperty().addListener((observable, oldValue, newValue) -> {
            resetErrorsInfoExercise();
        });

        setupButtons();
    }

    private void setupButtons() {
        backBtn.setOnAction(this::handleBackStep);
        nextBtn.setOnAction(this::handleNextStep);
        createBtn.setOnAction(this::handleCreateExercise);
    }

    /**
     * Gestisce l'evento di creazione finale dell'esercizio.
     * Assembla il DTO completo dell'esercizio e invoca il servizio per salvarlo.
     * Mostra una notifica di successo o di errore.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handleCreateExercise(Event event) {
        ExerciseDto exerciseDto = new ExerciseDto(infoExerciseDto, fieldDifficulty.getValue(), questions);
        try {
            ExerciseService.createExercise(exerciseDto);
            clearFields();
            removeStyles();
            resetQuestions();
            currentStep = 0;
            updateStepState();
            String title = I18NUtils.getUI(SUBFOLDER_UI, NOTIFY_TITLE_SUCC, SessionManager.getInstance().getCurrentLanguage().getLocale());
            String message = I18NUtils.getUI(SUBFOLDER_UI, NOTIFY_MESSAGE_SUCC, SessionManager.getInstance().getCurrentLanguage().getLocale());
            showNotification(NotificationType.SUCCESS, title, message);
        } catch (ExerciseServiceException e) {
            LOG.log(Level.SEVERE, "Errore durante la creazione dell'esercizio");
            String title = I18NUtils.getError(SUBFOLDER_UI, ERROR_CREATE_TITLE, SessionManager.getInstance().getCurrentLanguage().getLocale());
            String message = I18NUtils.getError(SUBFOLDER_UI, ERROR_CREATE_MESSAGE, SessionManager.getInstance().getCurrentLanguage().getLocale());
            showNotification(NotificationType.ERROR, title, message);
        }
    }

    /**
     * Gestisce il passaggio allo step successivo del wizard di creazione.
     * Valida lo step corrente prima di procedere.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handleNextStep(Event event) {
        if (currentStep == 0) { // Dalle info alla prima domanda
            if (validateInfoStep()) {
                infoExerciseDto = new InfoExerciseDto(fieldTitle.getText(),
                        fieldDescription.getText(),
                        fieldCategory.getValue(),
                        fieldTopic.getValue());
                currentStep++;
                // Carica la domanda 1 se esiste, altrimenti pulisce i campi
                QuestionDto firstQuestion = questions.get(0);
                if (firstQuestion != null) {
                    forumQuestionController.loadQuestionData(firstQuestion);
                } else {
                    forumQuestionController.clearField();
                }
            }
        } else if (currentStep <= TOTAL_QUESTION) { // Dalle domande successive al riepilogo
            if (forumQuestionController.validateQuestion()) {
                questions.set(currentStep - 1, forumQuestionController.getQuestionData());
                currentStep++;
                if (currentStep <= TOTAL_QUESTION) {
                    QuestionDto nextQuestion = questions.get(currentStep - 1);
                    if (nextQuestion != null) {
                        forumQuestionController.loadQuestionData(nextQuestion);
                    } else {
                        forumQuestionController.clearField();
                    }
                }
            }
        }
        updateStepState();
    }

    /**
     * Gestisce il ritorno allo step precedente del wizard di creazione.
     * Salva lo stato della domanda corrente (se valida) prima di tornare indietro.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handleBackStep(Event event) {
        if (currentStep > 0) {
            // Salva lo stato della domanda corrente prima di tornare indietro (se si è in una vista domanda)
            if (currentStep <= TOTAL_QUESTION) {
                // Salva la domanda corrente solo se i dati sono validi
                if (forumQuestionController.validateQuestion()) {
                    questions.set(currentStep - 1, forumQuestionController.getQuestionData());
                }
            }
            currentStep--;
            if (currentStep > 0) { // Se torno a una domanda
                forumQuestionController.loadQuestionData(questions.get(currentStep - 1));
            }
        }
        updateStepState();
    }

    /**
     * Valida i campi relativi alle informazioni generali dell'esercizio (titolo, descrizione, ecc.).
     * Mostra messaggi di errore se la validazione fallisce.
     *
     * @return true se i dati sono validi, altrimenti false.
     */
    private boolean validateInfoStep() {
        resetErrorsInfoExercise();
        InfoExerciseDto infoExerciseDto = new InfoExerciseDto(
                fieldTitle.getText(),
                fieldDescription.getText(),
                fieldCategory.getValue(),
                fieldTopic.getValue()
        );
        ErrorCollector errorCollector = new ErrorCollector();
        boolean isValid = ExerciseService.validateInfoExerciseStep(infoExerciseDto, fieldDifficulty.getValue(), errorCollector);

        if (!isValid) {
            if (errorCollector.hasErrors()) {
                for (ApplicationException e : errorCollector.getErrors()) {
                    if (e.getErrorCode() == INFO_EXERCISE_TITLE_INVALID) {
                        errorTitle.setVisible(true);
                        errorTitle.setText(I18NUtils.getError(SUBFOLDER_ERROR, e.getErrorCode().getMessageKey(),
                                SessionManager.getInstance().getCurrentLanguage().getLocale()));
                        fieldTitle.getStyleClass().addAll("invalid-filed");

                    } else if (e.getErrorCode() == ALREADY_TITLE_EXISTS) {
                        errorTitle.setVisible(true);
                        errorTitle.setText(I18NUtils.getError(SUBFOLDER_ERROR, ALREADY_TITLE_EXISTS.getTitleKey(),
                                SessionManager.getInstance().getCurrentLanguage().getLocale()));
                        fieldTitle.getStyleClass().addAll("invalid-filed");

                    }  else if (e.getErrorCode() == INFO_EXERCISE_DESCRIPTION_INVALID) {
                        errorDescription.setVisible(true);
                        errorDescription.setText(I18NUtils.getError(SUBFOLDER_ERROR, e.getErrorCode().getMessageKey(),
                                SessionManager.getInstance().getCurrentLanguage().getLocale()));
                        fieldDescription.getStyleClass().addAll("invalid-filed");

                    } else if (e.getErrorCode() == INFO_EXERCISE_CATEGORY_INVALID) {
                        errorCategory.setVisible(true);
                        errorCategory.setText(I18NUtils.getError(SUBFOLDER_ERROR, e.getErrorCode().getMessageKey(),
                                SessionManager.getInstance().getCurrentLanguage().getLocale()));
                        fieldCategory.getStyleClass().addAll("invalid-filed", "invalid-icon");

                    } else if (e.getErrorCode() == INFO_EXERCISE_TOPIC_INVALID) {
                        errorTopic.setVisible(true);
                        errorTopic.setText(I18NUtils.getError(SUBFOLDER_ERROR, e.getErrorCode().getMessageKey(),
                                SessionManager.getInstance().getCurrentLanguage().getLocale()));
                        fieldTopic.getStyleClass().addAll("invalid-filed", "invalid-icon");
                    } else if (e.getErrorCode() == EXERCISE_DIFFICULTY_EMPTY) {
                        errorDifficulty.setVisible(true);
                        errorDifficulty.setText(I18NUtils.getError(SUBFOLDER_ERROR, e.getErrorCode().getMessageKey(),
                                SessionManager.getInstance().getCurrentLanguage().getLocale()));
                        fieldDifficulty.getStyleClass().addAll("invalid-filed", "invalid-icon");
                    }
                }
            }
        }
        return isValid;
    }


    /**
     * Popola le ComboBox con i valori degli enum (Difficulty, Topic, Category).
     */
    private void populateComboBoxes() {
        fieldDifficulty.getItems().setAll(Difficulty.values());
        fieldDifficulty.setConverter(createDifficultyConverter());

        fieldTopic.getItems().setAll(Topic.values());
        fieldTopic.setConverter(createTopicConverter());

        fieldCategory.getItems().setAll(Category.values());
        fieldCategory.setConverter(createCategoryConverter());
    }

    /**
     * Crea e restituisce un StringConverter per l'enum Difficulty,
     * per gestire la visualizzazione internazionalizzata.
     *
     * @return Un StringConverter per Difficulty.
     */
    private StringConverter<Difficulty> createDifficultyConverter() {
        final String PATH_DIFFICULTY = "difficulty";
        return new StringConverter<>() {
            @Override
            public String toString(Difficulty difficulty) {
                if (difficulty == null) return "";
                return I18NUtils.getEnum(PATH_DIFFICULTY, difficulty.getTextUIKey(), SessionManager.getInstance().getCurrentLanguage().getLocale());
            }

            @Override
            public Difficulty fromString(String string) {
                return null;
            }
        };
    }

    private StringConverter<Topic> createTopicConverter() {
        final String PATH_TOPIC = "topic";
        return new StringConverter<>() {
            @Override
            public String toString(Topic topic) {
                if (topic == null) return "";
                return I18NUtils.getEnum(PATH_TOPIC, topic.getTextUIKeyy(), SessionManager.getInstance().getCurrentLanguage().getLocale());
            }

            @Override
            public Topic fromString(String string) {
                return null;
            }
        };
    }

    private StringConverter<Category> createCategoryConverter() {
        final String PATH_CATEGORY = "category";
        return new StringConverter<>() {
            @Override
            public String toString(Category category) {
                if (category == null) return "";
                return I18NUtils.getEnum(PATH_CATEGORY, category.textUIKey(), SessionManager.getInstance().getCurrentLanguage().getLocale());
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        };
    }

    /**
     * Aggiorna lo stato della UI in base allo step corrente (titolo della pagina, visibilità dei pulsanti e dei pannelli).
     */
    private void updateStepState() {
        Locale loc = SessionManager.getInstance().getCurrentLanguage().getLocale();
        if (currentStep == 0) {
            titlePage.setText(I18NUtils.getUI(SUBFOLDER_UI, FORUM_TITLE, loc));
        } else if (currentStep <= TOTAL_QUESTION) {
            String questionPattern = I18NUtils.getUI(SUBFOLDER_UI, QUESTION_TITLE, loc);
            titlePage.setText(String.format(questionPattern, currentStep, TOTAL_QUESTION));
        } else {
            titlePage.setText(I18NUtils.getUI(SUBFOLDER_UI, RECAP_TITLE, loc));
        }

        backBtn.setVisible(currentStep > 0);
        nextBtn.setVisible(currentStep < TOTAL_QUESTION + 1);
        createBtn.setVisible(currentStep == TOTAL_QUESTION + 1);

        if (currentStep == 0) {
            switchToInfoView();
        } else if (currentStep <= TOTAL_QUESTION) {
            switchToQuestionView();
        } else {
            switchToRecapView();
        }
    }

    private void switchToRecapView() {
        hidePaneInfoExercise();
        hidePaneCreateQuestion();
        showPaneRecapExercise();
        recapExerciseController.populate(infoExerciseDto, fieldDifficulty.getValue(), questions);
    }

    private void switchToQuestionView() {
        hidePaneInfoExercise();
        hidePaneRecapExercise();
        showPaneCreateQuestion();
    }

    private void switchToInfoView() {
        hidePaneCreateQuestion();
        hidePaneRecapExercise();
        showPaneInfoExercise();
    }

    private void hidePaneCreateQuestion() {
        containerForumQuestion.setManaged(false);
        containerForumQuestion.setVisible(false);
    }
    private void showPaneCreateQuestion() {
        containerForumQuestion.setManaged(true);
        containerForumQuestion.setVisible(true);
    }

    private void hidePaneInfoExercise() {
        containerForumInfoExercise.setManaged(false);
        containerForumInfoExercise.setVisible(false);
    }
    private void showPaneInfoExercise() {
        containerForumInfoExercise.setManaged(true);
        containerForumInfoExercise.setVisible(true);
    }

    private void hidePaneRecapExercise() {
        containerRecapExercise.setManaged(false);
        containerRecapExercise.setVisible(false);
    }
    private void showPaneRecapExercise() {
        containerRecapExercise.setManaged(true);
        containerRecapExercise.setVisible(true);
    }



    private void hideErrors() {
        errorTitle.setVisible(false);
        errorDifficulty.setVisible(false);
        errorDescription.setVisible(false);
        errorTopic.setVisible(false);
        errorCategory.setVisible(false);
    }

    private void resetErrorsInfoExercise() {
        hideErrors();
        fieldTitle.getStyleClass().removeAll("invalid-filed");
        fieldDescription.getStyleClass().removeAll("invalid-filed");
        fieldCategory.getStyleClass().removeAll("invalid-filed", "invalid-icon");
        fieldTopic.getStyleClass().removeAll("invalid-filed", "invalid-icon");
        fieldDifficulty.getStyleClass().removeAll("invalid-filed", "invalid-icon");
    }

    private void clearFields() {
        fieldTitle.clear();
        fieldDifficulty.getSelectionModel().clearSelection();
        fieldDescription.clear();
        fieldTopic.getSelectionModel().clearSelection();
        fieldCategory.getSelectionModel().clearSelection();
    }

    private void resetQuestions() {
        Collections.fill(questions, null);
        forumQuestionController.clearField();
    }

    private void removeStyles() {
        fieldTitle.getStyleClass().remove("invalid-field");
        fieldDifficulty.getStyleClass().remove("invalid-field");
        fieldDescription.getStyleClass().remove("invalid-field");
        fieldTopic.getStyleClass().remove("invalid-field");
        fieldCategory.getStyleClass().remove("invalid-field");
    }

    /**
     * Aggiorna i converter delle ComboBox per riflettere la lingua corrente,
     * preservando la selezione attuale.
     */
    private void updateComboBoxes() {
        Difficulty selectedDifficulty = fieldDifficulty.getValue();
        Topic selectedTopic = fieldTopic.getValue();
        Category selectedCategory = fieldCategory.getValue();

        fieldDifficulty.setConverter(createDifficultyConverter());
        fieldTopic.setConverter(createTopicConverter());
        fieldCategory.setConverter(createCategoryConverter());

        fieldDifficulty.setValue(null);
        fieldDifficulty.setValue(selectedDifficulty);

        fieldTopic.setValue(null);
        fieldTopic.setValue(selectedTopic);

        fieldCategory.setValue(null);
        fieldCategory.setValue(selectedCategory);
    }


    private void updateText(Language lang) {
        if (currentStep == 0) {
            titlePage.setText(I18NUtils.getUI(SUBFOLDER_UI, FORUM_TITLE, lang.getLocale()));
        } else if (currentStep <= TOTAL_QUESTION) {
            String questionPattern = I18NUtils.getUI(SUBFOLDER_UI, QUESTION_TITLE, lang.getLocale());
            titlePage.setText(String.format(questionPattern, currentStep, TOTAL_QUESTION));
        } else {
            titlePage.setText(I18NUtils.getUI(SUBFOLDER_UI, RECAP_TITLE, lang.getLocale()));
        }
        titleForum.setText(I18NUtils.getUI(SUBFOLDER_UI, FORUM_SUBTITLE, lang.getLocale()));
        floatingTitle.setText(I18NUtils.getUI(SUBFOLDER_UI, FLOATING_TEXT_TITLE, lang.getLocale()));
        fieldTitle.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_TITLE, lang.getLocale()));
        floatingDifficulty.setText(I18NUtils.getUI(SUBFOLDER_UI, DIFFICULTY, lang.getLocale()));
        fieldDifficulty.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_DIFFICULTY, lang.getLocale()));
        floatingDescription.setText(I18NUtils.getUI(SUBFOLDER_UI, FLOATING_DESCRIPTION, lang.getLocale()));
        fieldDescription.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_DESCRIPTION, lang.getLocale()));
        floatingTopic.setText(I18NUtils.getUI(SUBFOLDER_UI, TOPIC, lang.getLocale()));
        fieldTopic.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_TOPIC, lang.getLocale()));
        floatingCategory.setText(I18NUtils.getUI(SUBFOLDER_UI, CATEGORY, lang.getLocale()));
        fieldCategory.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_CATEGORY, lang.getLocale()));
        backBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, BACK_BUTTON, lang.getLocale()));
        nextBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, NEXT_BUTTON, lang.getLocale()));
        createBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, CREATE_BUTTON, lang.getLocale()));

        updateComboBoxes();
    }

    /**
     * Mostra una notifica a schermo.
     *
     * @param type    Il tipo di notifica (es. SUCCESS, ERROR).
     * @param title   Il titolo della notifica.
     * @param message Il messaggio della notifica.
     */
    private void showNotification(NotificationType type, String title, String message) {
        if (rootPane == null) {
            LOG.log(Level.SEVERE, "Root pane non impostato, impossibile mostrare la notifica.");
            return;
        }
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

}
