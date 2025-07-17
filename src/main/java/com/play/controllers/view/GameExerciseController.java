package com.play.controllers.view;

import com.play.controllers.component.CodeAreaController;
import com.play.controllers.component.RecapExerciseController;
import com.play.controllers.util.ExerciseSession;
import com.play.controllers.util.SceneLoader;
import com.play.controllers.util.SessionManager;
import com.play.model.entity.exercise.Answer;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.exercise.Question;
import com.play.model.entity.user.User;
import com.play.model.enums.Language;
import com.play.service.ScoreService;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Controller che gestisce la logica della schermata di gioco di un esercizio.
 * Permette all'utente di navigare tra le domande, rispondere, e visualizzare
 * i risultati finali e un riepilogo dettagliato.
 */
public class GameExerciseController implements Initializable {

    @FXML private StackPane mainContainer;
    @FXML private VBox exerciseContainer;
    @FXML private VBox questionContainer;
    @FXML private VBox recapContainer;
    @FXML private VBox dialogClosePage;
    @FXML private VBox dialogResult;

    @FXML private MFXButton backBtn;
    @FXML private MFXButton nextBtn;
    @FXML private MFXButton confirmBtn;
    @FXML private MFXButton openDialogBtn;
    @FXML private MFXButton closeDialgBtn;
    @FXML private MFXButton cancelDialogBtn;
    @FXML private MFXButton confirmDialogBtn;
    @FXML private MFXButton viewResultBtn;
    @FXML private MFXButton goHomeBtn;
    @FXML private MFXButton newExerciseBtn;

    @FXML private Label titlePage;
    @FXML private Label currentQuestion;
    @FXML private Label totalQuestion;
    @FXML private Label floatQuestion;
    @FXML private Label floatA;
    @FXML private Label floatB;
    @FXML private Label floatC;
    @FXML private Label floatD;
    @FXML private Label dialogTitle;
    @FXML private Label messageDialog;
    @FXML private Label titleDialogResult;
    @FXML private Label scoreText;
    @FXML private Label scoreUser;
    @FXML private Label scoreExercise;
    @FXML private Label separetorDialog;
    @FXML private Label perctResult;
    @FXML private Label correctText;
    @FXML private Label errorText;
    @FXML private Label skippedText;
    @FXML private Label precisionText;

    @FXML private Label numberCorrect;
    @FXML private Label numberError;
    @FXML private Label numberSkipped;

    @FXML private TextField fieldA;
    @FXML private TextField fieldB;
    @FXML private TextField fieldC;
    @FXML private TextField fieldD;
    @FXML private MFXCheckbox checkA;
    @FXML private MFXCheckbox checkB;
    @FXML private MFXCheckbox checkC;
    @FXML private MFXCheckbox checkD;

    @FXML private MFXProgressBar progessExercise;

    @FXML private CodeAreaController codeAreaController;
    @FXML private RecapExerciseController recapExerciseController;

    /** Sessione di gioco corrente che tiene traccia dello stato dell'esercizio. */
    private ExerciseSession exerciseSession;
    /** Lista dei campi di testo per le risposte. */
    private List<TextField> fields;
    /** Lista delle checkbox per la selezione delle risposte. */
    private List<MFXCheckbox> checkBoxes;
    /** Gruppo per garantire la selezione singola tra le checkbox. */
    private ToggleGroup answerGroup;
    /** Lista dei pannelli principali per una facile gestione della visibilità. */
    private List<VBox> mainPanes;

    private static final String SUBFOLDER_DEFAULT_EXERCISE = "exercise";
    private static final String FOLDER_UI = "game-exercise";
    private static final String FLOAT_QUESTION = "floatQuestion";
    private static final String FLOAT_A = "floatA";
    private static final String FLOAT_B = "floatB";
    private static final String FLOAT_C = "floatC";
    private static final String FLOAT_D = "floatD";
    private static final String TITLE_PAGE_RESULTS = "titlePageRecap";
    private static final String BACK_BTN = "backBtn";
    private static final String NEXT_BTN = "nextBtn";
    private static final String CONFIRM_BTN = "confirmBtn";
    private static final String TITLE_DIALOG_CLOSE = "closeDialogTitle";
    private static final String MESSAGE_DIALOG_CLOSE = "closeDialogText";
    private static final String CONF_DIALOG_CLOSE = "confirmCloseDialogBtn";
    private static final String CANCEL_DIALOG_CLOSE = "cancelCloseDialogBtn";
    private static final String TITLE_DIALOG_RESULT = "resultsDialogTitle";
    private static final String SCORE_TEXT = "scoreText";
    private static final String PRECISION_TEXT = "precisionText";
    private static final String CORRECT_TEXT = "correctText";
    private static final String INCORRECT_TEXT = "incorrectText";
    private static final String SKIPPED_TEXT = "skippedText";
    private static final String NEW_EXERCISE_TEXT = "newExerciseBtn";
    private static final String VIEW_RESULT_TEXT = "viewResultsBtn";
    private static final String GO_HOME_TEXT = "backHomeBtn";
    private static final String SEPARATOR = "separator";
    // endregion

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fields = Arrays.asList(fieldA, fieldB, fieldC, fieldD);
        checkBoxes = Arrays.asList(checkA, checkB, checkC, checkD);
        mainPanes = Arrays.asList(exerciseContainer, recapContainer, dialogClosePage, dialogResult);
        answerGroup = new ToggleGroup();

        setupSingleSelectionForCheckboxes();
        setUpButtons();

        updateText(SessionManager.getInstance().getCurrentLanguage());
        SessionManager.getInstance().currentLanguageProperty().addListener((obs, o, n) -> updateText(n));
    }

    /**
     * Carica un esercizio per iniziare la sessione di gioco.
     *
     * @param exercise L'esercizio da svolgere.
     */
    public void loadExercise(Exercise exercise) {
        this.exerciseSession = new ExerciseSession(exercise);
        loadCurrentQuestion();
        showPane(exerciseContainer);
    }

    private void setUpButtons() {
        backBtn.setOnAction(this::handleBack);
        nextBtn.setOnAction(this::handleNext);
        confirmBtn.setOnAction(this::handleConfirm);

        openDialogBtn.setOnAction(event -> showPane(dialogClosePage));
        closeDialgBtn.setOnAction(event -> showPane(exerciseContainer));
        cancelDialogBtn.setOnAction(event -> showPane(exerciseContainer));
        confirmDialogBtn.setOnAction(this::handleNewExercise);

        viewResultBtn.setOnAction(this::handleViewResult);
        goHomeBtn.setOnAction(this::handleGoHome);
        newExerciseBtn.setOnAction(this::handleNewExercise);
    }

    private void handleNewExercise(ActionEvent event) {
        final String PATH = "ExerciseStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, (Node) event.getSource());
    }

    private void handleGoHome(ActionEvent event) {
        final String PATH = "HomepageStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, (Node) event.getSource());
    }

    private void handleExit(ActionEvent event) {
        final String PATH = "ExerciseStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, (Node) event.getSource());
    }

    /**
     * Carica i dati della domanda corrente nella UI, inclusi il testo della domanda
     * e le opzioni di risposta.
     */
    private void loadCurrentQuestion() {
        Question question = exerciseSession.getCurrentQuestion();
        if (question == null) return;

        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setVisible(true);
            checkBoxes.get(i).setVisible(true);
        }

        codeAreaController.setText(question.getQuestionText());
        codeAreaController.formatJavaCode();
        codeAreaController.enableHighlighting();

        List<Answer> answers = question.getAnswers();
        for (int i = 0; i < fields.size(); i++) {
            boolean hasAnswer = i < answers.size();
            fields.get(i).setVisible(hasAnswer);
            checkBoxes.get(i).setVisible(hasAnswer);
            if (hasAnswer) {
                fields.get(i).setText(answers.get(i).getText());
            }
        }
        answerSelection();
        updateState();
    }

    /**
     * Registra la risposta selezionata dall'utente per la domanda corrente nella sessione di gioco.
     */
    private void recordAnswer() {
        int selectedIndex = -1;
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                selectedIndex = i;
                break;
            }
        }
        Answer selectedAnswer = null;
        if (selectedIndex != -1) {
            selectedAnswer = exerciseSession.getCurrentQuestion().getAnswers().get(selectedIndex);
        }
        exerciseSession.recordAnswer(selectedAnswer);
    }

    /**
     * Imposta la selezione della checkbox in base alla risposta precedentemente data
     * per la domanda corrente, se presente.
     */
    private void answerSelection() {
        answerGroup.selectToggle(null);
        for (MFXCheckbox checkbox : checkBoxes) {
            checkbox.setSelected(false);
        }

        Answer preview = exerciseSession.getAnswerForCurrentQuestion();
        if (preview != null) {
            int answerIndex = exerciseSession.getCurrentQuestion().getAnswers().indexOf(preview);
            if (answerIndex != -1) {
                checkBoxes.get(answerIndex).setSelected(true);
            }
        }
    }

    /**
     * Aggiorna lo stato della UI (titolo, barra di progresso, contatore domande, visibilità pulsanti).
     */
    private void updateState() {
        titlePage.setText(I18NUtils.translateIfKey(SUBFOLDER_DEFAULT_EXERCISE, exerciseSession.getExercise().getInfoExercise().getTitle(),
                SessionManager.getInstance().getCurrentLanguage().getLocale()));
        currentQuestion.setText(String.valueOf(exerciseSession.getCurrentQuestionNumber()));
        totalQuestion.setText(String.valueOf(exerciseSession.getTotalQuestions()));

        double progress = 0.0;
        if (exerciseSession.getTotalQuestions() > 1) {
            progress = (double) exerciseSession.getCurrentQuestionIndex() / (exerciseSession.getTotalQuestions() - 1);
        } else if (exerciseSession.getTotalQuestions() == 1) {
            progress = 1.0;
        }
        progessExercise.setProgress(progress);

        backBtn.setDisable(!exerciseSession.canMoveToPreviousQuestion());
        nextBtn.setVisible(!exerciseSession.isLastQuestion());
        confirmBtn.setVisible(exerciseSession.isLastQuestion());
    }

    /**
     * Gestisce il passaggio alla domanda successiva, registrando prima la risposta corrente.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handleNext(Event event) {
        recordAnswer();
        if (exerciseSession.moveToNextQuestion()) {
            loadCurrentQuestion();
        }
    }

    /**
     * Gestisce il ritorno alla domanda precedente, registrando prima la risposta corrente.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handleBack(Event event) {
        recordAnswer();
        if (exerciseSession.moveToPreviousQuestion()) {
            loadCurrentQuestion();
        }
    }

    /**
     * Gestisce la conferma finale dell'esercizio. Registra l'ultima risposta,
     * calcola il punteggio, lo salva e mostra il dialogo dei risultati.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handleConfirm(Event event) {
        recordAnswer();
        int finalScore = exerciseSession.calculateScore();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        Exercise exercise = exerciseSession.getExercise();

        if (currentUser != null) {
            ScoreService.saveOrUpdateBestScore(currentUser, exercise, finalScore);
        }

        showResultDialog(finalScore);
    }

    /**
     * Gestisce la visualizzazione della schermata di riepilogo dettagliato.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void handleViewResult(ActionEvent event) {
        showRecapView();
    }

    /**
     * Configura le checkbox per consentire una sola selezione alla volta.
     */
    private void setupSingleSelectionForCheckboxes() {
        for (MFXCheckbox currentCheckbox : checkBoxes) {
            currentCheckbox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    for (MFXCheckbox otherCheckbox : checkBoxes) {
                        if (otherCheckbox != currentCheckbox) {
                            otherCheckbox.setSelected(false);
                        }
                    }
                }
            });
        }
    }

    private void updateText(Language lang) {
        floatQuestion.setText(I18NUtils.getUI(FOLDER_UI, FLOAT_QUESTION, lang.getLocale()));
        floatA.setText(I18NUtils.getUI(FOLDER_UI, FLOAT_A, lang.getLocale()));
        floatB.setText(I18NUtils.getUI(FOLDER_UI, FLOAT_B, lang.getLocale()));
        floatC.setText(I18NUtils.getUI(FOLDER_UI, FLOAT_C, lang.getLocale()));
        floatD.setText(I18NUtils.getUI(FOLDER_UI, FLOAT_D, lang.getLocale()));
        backBtn.setText(I18NUtils.getUI(FOLDER_UI, BACK_BTN, lang.getLocale()));
        nextBtn.setText(I18NUtils.getUI(FOLDER_UI, NEXT_BTN, lang.getLocale()));
        confirmBtn.setText(I18NUtils.getUI(FOLDER_UI, CONFIRM_BTN, lang.getLocale()));

        dialogTitle.setText(I18NUtils.getUI(FOLDER_UI, TITLE_DIALOG_CLOSE, lang.getLocale()));
        messageDialog.setText(I18NUtils.getUI(FOLDER_UI, MESSAGE_DIALOG_CLOSE, lang.getLocale()));
        confirmDialogBtn.setText(I18NUtils.getUI(FOLDER_UI, CONF_DIALOG_CLOSE, lang.getLocale()));
        cancelDialogBtn.setText(I18NUtils.getUI(FOLDER_UI, CANCEL_DIALOG_CLOSE, lang.getLocale()));

        titleDialogResult.setText(I18NUtils.getUI(FOLDER_UI, TITLE_DIALOG_RESULT, lang.getLocale()));
        scoreText.setText(I18NUtils.getUI(FOLDER_UI, SCORE_TEXT, lang.getLocale()));
        precisionText.setText(I18NUtils.getUI(FOLDER_UI, PRECISION_TEXT, lang.getLocale()));
        correctText.setText(I18NUtils.getUI(FOLDER_UI, CORRECT_TEXT, lang.getLocale()));
        errorText.setText(I18NUtils.getUI(FOLDER_UI, INCORRECT_TEXT, lang.getLocale()));
        skippedText.setText(I18NUtils.getUI(FOLDER_UI, SKIPPED_TEXT, lang.getLocale()));

        newExerciseBtn.setText(I18NUtils.getUI(FOLDER_UI, NEW_EXERCISE_TEXT, lang.getLocale()));
        viewResultBtn.setText(I18NUtils.getUI(FOLDER_UI, VIEW_RESULT_TEXT, lang.getLocale()));
        goHomeBtn.setText(I18NUtils.getUI(FOLDER_UI, GO_HOME_TEXT, lang.getLocale()));
        separetorDialog.setText(I18NUtils.getUI(FOLDER_UI, SEPARATOR, lang.getLocale()));

    }

    /**
     * Mostra un dialogo con i risultati finali dell'esercizio (punteggio, statistiche).
     *
     * @param finalScore Il punteggio finale ottenuto dall'utente.
     */
    private void showResultDialog(int finalScore) {
        Map<String, Integer> stats = exerciseSession.getExerciseStats();
        int correct = stats.getOrDefault("correct", 0);
        int incorrect = stats.getOrDefault("incorrect", 0);
        int skipped = stats.getOrDefault("skipped", 0);
        int totalQuestions = exerciseSession.getTotalQuestions();
        int maxScore = totalQuestions * exerciseSession.getExercise().getDifficulty().getMultiplier();

        double accuracy = (totalQuestions > 0) ? ((double) correct / totalQuestions) * 100 : 0;
        DecimalFormat df = new DecimalFormat("#.##");

        String titleText = I18NUtils.getUI(FOLDER_UI, TITLE_PAGE_RESULTS, SessionManager.getInstance().getCurrentLanguage().getLocale());
        titlePage.setText(titleText);

        scoreUser.setText(String.valueOf(finalScore));
        scoreExercise.setText(String.valueOf(maxScore));
        perctResult.setText(df.format(accuracy) + "%");
        numberCorrect.setText(String.valueOf(correct));
        numberError.setText(String.valueOf(incorrect));
        numberSkipped.setText(String.valueOf(skipped));

        showPane(dialogResult);
        backBtn.setVisible(false);
        openDialogBtn.setVisible(false);
        backBtn.setOnAction(this::handleBack); // Ripristina l'azione originale
    }

    /**
     * Mostra la vista di riepilogo con i dettagli delle risposte date.
     * Configura i pulsanti per la navigazione da questa vista.
     */
    private void showRecapView() {
        showPane(recapContainer);

        backBtn.setVisible(true);
        backBtn.setDisable(false);
        backBtn.setOnAction(event -> showResultDialog(exerciseSession.calculateScore()));
        nextBtn.setVisible(false);
        confirmBtn.setVisible(false);
        openDialogBtn.setVisible(false);

        String titleText = I18NUtils.getUI(FOLDER_UI, TITLE_PAGE_RESULTS, SessionManager.getInstance().getCurrentLanguage().getLocale());
        titlePage.setText(titleText);

        recapExerciseController.populateWithResults(exerciseSession);
    }

    /**
     * Mostra il pannello specificato e nasconde tutti gli altri pannelli principali.
     *
     * @param paneToShow Il VBox da rendere visibile e gestito.
     */
    private void showPane(VBox paneToShow) {
        for (VBox pane : mainPanes) {
            boolean isVisible = (pane == paneToShow);
            pane.setVisible(isVisible);
            pane.setManaged(isVisible);
        }
    }
}
