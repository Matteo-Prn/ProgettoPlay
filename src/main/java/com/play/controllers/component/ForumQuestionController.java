package com.play.controllers.component;

import com.play.controllers.util.IconUtils;
import com.play.controllers.util.SessionManager;
import com.play.dto.exercise.AnswerDto;
import com.play.dto.exercise.QuestionDto;
import com.play.exceptions.ApplicationException;
import com.play.model.entity.exercise.Answer;
import com.play.model.entity.exercise.Question;
import com.play.model.enums.Language;
import com.play.service.ExerciseService;
import com.play.util.provider.I18NUtils;
import com.play.util.validation.ErrorCollector;
import com.play.util.validation.QuestionValidationField;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

/**
 * Controller per il componente FXML che rappresenta un forum per una singola domanda.
 * Gestisce la creazione, la visualizzazione, la validazione e il riepilogo di una domanda
 * con le sue risposte. Include una modalità di modifica e una di anteprima.
 */
public class ForumQuestionController implements Initializable {

    @FXML
    private Label floatingQuestion;

    @FXML
    private VBox containerCodeWrite;

    @FXML
    private CodeAreaController coreAreaWriteController;

    @FXML
    private VBox containerCodePreview;

    @FXML
    private CodeAreaController coreAreaPreviewController;

    @FXML
    private Label errorQuestion;

    @FXML
    private MFXButton switchBtn;

    @FXML
    private FontIcon iconSwitchBtn;

    @FXML
    private MFXCheckbox isValidA;

    @FXML
    private Label floatingAnswerA;

    @FXML
    private TextField fieldAnswerA;

    @FXML
    private Label errorAnswerA;

    @FXML
    private MFXCheckbox isValidB;

    @FXML
    private Label floatingAnswerB;

    @FXML
    private TextField fieldAnswerB;

    @FXML
    private Label errorAnswerB;

    @FXML
    private MFXCheckbox isValidC;

    @FXML
    private Label floatingAnswerC;

    @FXML
    private TextField fieldAnswerC;

    @FXML
    private Label errorAnswerC;

    @FXML
    private MFXCheckbox isValidD;

    @FXML
    private Label floatingAnswerD;

    @FXML
    private TextField fieldAnswerD;

    @FXML
    private Label errorAnswerD;

    /** Flag per tracciare se il componente è in modalità anteprima o modifica. */
    private boolean isPreviewMode = false;

    private static final String SUBFOLDER_UI = "forum_question";
    private static final String SUBFOLDER_ERROR = "exercise";

    private static final String FLOATING_QUESTION = "floatingQuestion";
    private static final String FLOATING_ANSWER_A = "floatingAnswerA";
    private static final String PROMPT_A = "promptA";
    private static final String FLOATING_ANSWER_B = "floatingAnswerB";
    private static final String PROMPT_B = "promptB";
    private static final String FLOATING_ANSWER_C = "floatingAnswerC";
    private static final String PROMPT_C = "promptC";
    private static final String FLOATING_ANSWER_D = "floatingAnswerD";
    private static final String PROMPT_D = "promptD";
    private static final String PREVIEW_BUTTON = "previewButton";
    private static final String EDIT_BUTTON = "editButton";

    /** Lista dei campi di testo per le risposte. */
    private List<TextField> answerFields;
    /** Lista delle checkbox per indicare la risposta corretta. */
    private List<MFXCheckbox> checkBoxes;
    /** Lista delle etichette per mostrare gli errori di validazione. */
    private List<Label> errorLabels;
    /** Lista delle etichette fluttuanti. */
    private List<Label> floatingLabels;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        answerFields = Arrays.asList(fieldAnswerA, fieldAnswerB, fieldAnswerC, fieldAnswerD);
        checkBoxes = Arrays.asList(isValidA, isValidB, isValidC, isValidD);
        errorLabels = Arrays.asList(errorAnswerA, errorAnswerB, errorAnswerC, errorAnswerD, errorQuestion);
        floatingLabels = Arrays.asList(floatingQuestion, floatingAnswerA, floatingAnswerB, floatingAnswerC, floatingAnswerD);

        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());
        hideError();
        setupButtons();
        setupCheckboxes();
        addChangeListeners();
    }

    private void setupButtons() {
        switchBtn.setOnAction(this::switchButtons);
    }

    /**
     * Imposta il componente in modalità sola lettura.
     * Passa alla modalità anteprima, nasconde il pulsante di switch e disabilita
     * tutti i campi di input e le checkbox.
     */
    public void setReadOnly() {
        // Passa alla modalità anteprima se non lo è già
        if (!isPreviewMode) {
            switchButtons(null);
        }
        // Nasconde il bottone per lo switch
        switchBtn.setVisible(false);

        // Disabilita l'editing e la cattura del mouse per entrambe le code area
        coreAreaWriteController.setReadOnly(true);
        coreAreaWriteController.getCodeArea().setMouseTransparent(true);
        coreAreaPreviewController.setReadOnly(true);
        coreAreaPreviewController.getCodeArea().setMouseTransparent(true);

        // Disabilita tutti i campi di risposta e i checkbox
        Stream.of(fieldAnswerA, fieldAnswerB, fieldAnswerC, fieldAnswerD)
                .forEach(field -> field.setEditable(false));

        Stream.of(isValidA, isValidB, isValidC, isValidD)
                .forEach(checkbox -> checkbox.setDisable(true));
    }



    /**
     * Carica i dati di una domanda e delle sue risposte nei campi della UI.
     *
     * @param data Il DTO {@link QuestionDto} contenente i dati da caricare.
     */
    public void loadQuestionData(QuestionDto data) {
        hideError();
        clearField();
        coreAreaWriteController.setText(data.questionText());

        List<AnswerDto> answers = data.answers();
        if (answers != null && answers.size() == 4) {
            fieldAnswerA.setText(answers.get(0).text());
            isValidA.setSelected(answers.get(0).isCorrect());

            fieldAnswerB.setText(answers.get(1).text());
            isValidB.setSelected(answers.get(1).isCorrect());

            fieldAnswerC.setText(answers.get(2).text());
            isValidC.setSelected(answers.get(2).isCorrect());

            fieldAnswerD.setText(answers.get(3).text());
            isValidD.setSelected(answers.get(3).isCorrect());
        }
    }

    /**
     * Raccoglie i dati inseriti dall'utente nei campi della UI e li restituisce come DTO.
     *
     * @return Un {@link QuestionDto} con i dati correnti del forum.
     */
    public QuestionDto getQuestionData() {
        List<AnswerDto> answers = new ArrayList<>();
        answers.add(new AnswerDto(fieldAnswerA.getText(), isValidA.isSelected()));
        answers.add(new AnswerDto(fieldAnswerB.getText(), isValidB.isSelected()));
        answers.add(new AnswerDto(fieldAnswerC.getText(), isValidC.isSelected()));
        answers.add(new AnswerDto(fieldAnswerD.getText(), isValidD.isSelected()));

        return  new QuestionDto(coreAreaWriteController.getText(), answers);
    }


    /**
     * Valida i dati correnti della domanda utilizzando il servizio di validazione.
     * Mostra gli errori nella UI se la validazione fallisce.
     *
     * @return true se la domanda è valida, false altrimenti.
     */
    public boolean validateQuestion() {
        hideError();
        QuestionDto questionDto = getQuestionData();
        ErrorCollector errorCollector = new ErrorCollector();

        boolean hasErrors = ExerciseService.validateSingleQuestion(questionDto, errorCollector);

        if (hasErrors) {
            errorCollector.errorMap().forEach(this::showError);
        }
        return !hasErrors;
    }

    /**
     * Mostra un messaggio di errore specifico per un campo di validazione.
     *
     * @param key L'identificatore del campo che ha generato l'errore.
     * @param error L'eccezione contenente i dettagli dell'errore.
     */
    private void showError(Object key, ApplicationException error) {
        if (!(key instanceof QuestionValidationField)) return;

        QuestionValidationField fieldKey = (QuestionValidationField) key;
        String message = I18NUtils.getError(SUBFOLDER_ERROR, error.getErrorCode().getMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale());
        final String styleClass = "invalid-field";

        switch (fieldKey) {
            case QUESTION_TEXT:
                errorQuestion.setText(message);
                errorQuestion.setVisible(true);
                coreAreaWriteController.getCodeArea().getStyleClass().add(styleClass);
                break;
            case ANSWER_A:
                errorAnswerA.setText(message);
                errorAnswerA.setVisible(true);
                fieldAnswerA.getStyleClass().add(styleClass);
                break;
            case ANSWER_B:
                errorAnswerB.setText(message);
                errorAnswerB.setVisible(true);
                fieldAnswerB.getStyleClass().add(styleClass);
                break;
            case ANSWER_C:
                errorAnswerC.setText(message);
                errorAnswerC.setVisible(true);
                fieldAnswerC.getStyleClass().add(styleClass);
                break;
            case ANSWER_D:
                errorAnswerD.setText(message);
                errorAnswerD.setVisible(true);
                fieldAnswerD.getStyleClass().add(styleClass);
                break;
            case NO_CORRECT_ANSWER_SELECTED:
                errorQuestion.setText(message);
                errorQuestion.setVisible(true);
                break;
        }
    }

    /**
     * Gestisce l'evento di click per passare dalla modalità modifica a quella di anteprima e viceversa.
     *
     * @param event L'evento che ha scatenato l'azione.
     */
    private void switchButtons(Event event) {
        isPreviewMode = !isPreviewMode;
        if (isPreviewMode) {

            String code = coreAreaWriteController.getText();
            coreAreaPreviewController.setText(code);
            coreAreaPreviewController.formatJavaCode();
            coreAreaPreviewController.enableHighlighting();

            activePreview();

            switchBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, EDIT_BUTTON,
                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
            iconSwitchBtn.setIconCode(IconUtils.getIconPencil());
        } else {
            setEditMode();
        }
    }

    /**
     * Imposta la UI in modalità di modifica.
     */
    private void setEditMode() {
        isPreviewMode = false;
        activeWrite();
        coreAreaWriteController.disableHighlighting();
        switchBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, PREVIEW_BUTTON, SessionManager.getInstance().getCurrentLanguage().getLocale()));
        iconSwitchBtn.setIconCode(IconUtils.getIconEye());
    }


    private void activePreview() {
        containerCodePreview.setVisible(true);
        containerCodeWrite.setVisible(false);
    }
    private void activeWrite() {
        containerCodePreview.setVisible(false);
        containerCodeWrite.setVisible(true);
    }

    private void hideError() {
        errorAnswerA.setVisible(false);
        errorAnswerB.setVisible(false);
        errorAnswerC.setVisible(false);
        errorAnswerD.setVisible(false);
        errorQuestion.setVisible(false);

        coreAreaWriteController.getCodeArea().getStyleClass().remove("invalid-field");
        fieldAnswerA.getStyleClass().remove("invalid-field");
        fieldAnswerB.getStyleClass().remove("invalid-field");
        fieldAnswerC.getStyleClass().remove("invalid-field");
        fieldAnswerD.getStyleClass().remove("invalid-field");
    }

    public void clearField() {
        hideError();
        setEditMode();
        coreAreaWriteController.setText("");
        fieldAnswerA.clear();
        fieldAnswerB.clear();
        fieldAnswerC.clear();
        fieldAnswerD.clear();
        isValidA.setSelected(false);
        isValidB.setSelected(false);
        isValidC.setSelected(false);
        isValidD.setSelected(false);
    }

    private void updateText(Language lang) {
        floatingQuestion.setText(I18NUtils.getUI(SUBFOLDER_UI, FLOATING_QUESTION, lang.getLocale()));
        floatingAnswerA.setText(I18NUtils.getUI(SUBFOLDER_UI, FLOATING_ANSWER_A, lang.getLocale()));
        fieldAnswerA.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_A, lang.getLocale()));
        floatingAnswerB.setText(I18NUtils.getUI(SUBFOLDER_UI, FLOATING_ANSWER_B, lang.getLocale()));
        fieldAnswerB.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_B, lang.getLocale()));
        floatingAnswerC.setText(I18NUtils.getUI(SUBFOLDER_UI, FLOATING_ANSWER_C, lang.getLocale()));
        fieldAnswerC.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_C, lang.getLocale()));
        floatingAnswerD.setText(I18NUtils.getUI(SUBFOLDER_UI, FLOATING_ANSWER_D, lang.getLocale()));
        fieldAnswerD.setPromptText(I18NUtils.getUI(SUBFOLDER_UI, PROMPT_D, lang.getLocale()));

        switchBtn.setText(I18NUtils.getUI(SUBFOLDER_UI, PREVIEW_BUTTON, SessionManager.getInstance().getCurrentLanguage().getLocale()));
        iconSwitchBtn.setIconCode(isPreviewMode ? IconUtils.getIconPencil() : IconUtils.getIconEye());
    }


    /**
     * Aggiunge listener ai campi di input per nascondere gli errori non appena l'utente modifica il contenuto.
     */
    private void addChangeListeners() {
        coreAreaWriteController.getCodeArea().textProperty().addListener((observable, oldValue, newValue) -> hideError());
        fieldAnswerA.textProperty().addListener((obs, o, n) -> hideError());
        fieldAnswerB.textProperty().addListener((obs, o, n) -> hideError());
        fieldAnswerC.textProperty().addListener((obs, o, n) -> hideError());
        fieldAnswerD.textProperty().addListener((obs, o, n) -> hideError());
        isValidA.selectedProperty().addListener((obs, o, n) -> hideError());
        isValidB.selectedProperty().addListener((obs, o, n) -> hideError());
        isValidC.selectedProperty().addListener((obs, o, n) -> hideError());
        isValidD.selectedProperty().addListener((obs, o, n) -> hideError());
    }

    /**
     * Configura le checkbox per garantire che solo una possa essere selezionata alla volta.
     */
    private void setupCheckboxes() {

        isValidA.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                isValidB.setSelected(false);
                isValidC.setSelected(false);
                isValidD.setSelected(false);
            }
        });
        isValidB.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                isValidA.setSelected(false);
                isValidC.setSelected(false);
                isValidD.setSelected(false);
            }
        });
        isValidC.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                isValidA.setSelected(false);
                isValidB.setSelected(false);
                isValidD.setSelected(false);
            }
        });
        isValidD.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                isValidA.setSelected(false);
                isValidB.setSelected(false);
                isValidC.setSelected(false);
            }
        });
    }

    /**
     * Restituisce uno stream delle checkbox attualmente selezionate.
     *
     * @return Uno {@link Stream} di {@link MFXCheckbox} selezionate.
     */
    private Stream<MFXCheckbox> getSelectedCheckboxes() {
        return Stream.of(isValidA, isValidB, isValidC, isValidD)
                .filter(MFXCheckbox::isSelected);
    }

    /**
     * Carica i dati di una domanda e della risposta data dall'utente per la visualizzazione nel riepilogo dei risultati.
     * Applica stili per evidenziare la risposta corretta e quella data dall'utente.
     *
     * @param question La domanda originale dell'esercizio.
     * @param userAnswer La risposta data dall'utente (può essere null se saltata).
     */
    public void loadRecapData(Question question, Answer userAnswer) {
        String questionText = question.getQuestionText();
        coreAreaWriteController.setText(questionText);
        coreAreaPreviewController.setText(questionText); // Imposta il testo anche nell'area di anteprima

        setReadOnly(); // Questo attiverà la modalità anteprima

        coreAreaPreviewController.formatJavaCode();
        coreAreaPreviewController.enableHighlighting();

        List<Answer> answers = question.getAnswers();
        for (int i = 0; i < answerFields.size(); i++) {
            boolean hasAnswer = i < answers.size();
            answerFields.get(i).getParent().getParent().setVisible(hasAnswer);
            checkBoxes.get(i).getParent().setVisible(hasAnswer);

            if (hasAnswer) {
                Answer currentAnswer = answers.get(i);
                answerFields.get(i).setText(currentAnswer.getText());
                MFXCheckbox checkbox = checkBoxes.get(i);

                // Stile per la risposta corretta/sbagliata
                if (currentAnswer.isCorrect()) {
                    checkbox.getStyleClass().add("correct-answer");
                } else {
                    checkbox.getStyleClass().add("incorrect-answer");
                }

                // Stile per la risposta selezionata dall'utente
                if (currentAnswer.equals(userAnswer)) {
                    checkbox.getStyleClass().add("user-selected");
                    checkbox.setSelected(true);
                } else {
                    checkbox.setSelected(false);
                }
            }
        }
    }
}
