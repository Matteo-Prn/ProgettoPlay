package com.play.controllers.view;

import com.play.controllers.component.SidebarAdminController;
import com.play.controllers.util.SceneLoader;
import com.play.controllers.util.SessionManager;
import com.play.dto.exercise.ExerciseDto;
import com.play.model.entity.user.User;
import com.play.model.enums.Category;
import com.play.model.enums.Language;
import com.play.service.ExerciseService;
import com.play.service.ScoreService;
import com.play.service.StudentService;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller per la homepage dell'amministratore.
 * Gestisce la visualizzazione delle statistiche, della classifica,
 * e la navigazione tra le categorie di esercizi.
 */
public class HomepageAdminController implements Initializable {
    @FXML
    private Label pageText;

    @FXML
    private Label userText;

    @FXML
    private Label userValue;

    @FXML
    private Label exerciseTotalText;

    @FXML
    private Label exerciseTotalValue;

    @FXML
    private Label exerciseSection;

    @FXML
    private MFXButton viewAllExerciseBtn;

    @FXML
    private Label categoryFindError;

    @FXML
    private TextArea descriptionFindError;

    @FXML
    private MFXButton findErrorBtn;

    @FXML
    private Label categoryOutput;

    @FXML
    private TextArea descriptionOutput;

    @FXML
    private MFXButton outputBtn;

    @FXML
    private Label categotyCompleteCode;

    @FXML
    private TextArea descriptionCompleteCode;

    @FXML
    private MFXButton completeCodeBtn;

    @FXML
    private SidebarAdminController sidebarAdminController;

    private static final String FOLDER_UI = "homepage-admin";
    private static final String CATEGORY_UI = "category";

    private static final String PAGE_TEXT = "pageText";
    private static final String USER_CARD = "titleUserCard";
    private static final String EXERCISE_CARD = "titleExerciseCard";;
    private static final String EXERCISE_SECTION = "exerciseSection";
    private static final String VIEW_ALL = "viewAll";
    private static final String GO_TO_EXERCISE = "goToExercise";

    private static final String CATEGORY_FIND = "find_error";
    private static final String CATEGORY_OUTPUT = "output";
    private static final String CATEGORY_COMPLETE_CODE = "solve_code";
    private static final String DESCRIPTION_FIND = "descriptionFind";
    private static final String DESCRIPTION_OUTPUT = "descriptionOutput";
    private static final String DESCRIPTION_COMPLETE_CODE = "descriptionSolve";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidebarAdminController.selectHomeButton();

        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());
        loadDashboardData();
        setButtons();
    }

    private void handleViewAllExercise(Event event) {
        final String PATH = "ExerciseAdminView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, viewAllExerciseBtn);
    }

    private void loadDashboardData() {
        List<ExerciseDto> allExercises = ExerciseService.getAllExercises();
        int totalCount = allExercises.size();
        exerciseTotalValue.setText(String.valueOf(totalCount));

        int totalStudents = StudentService.getTotalStudentCount();

        userValue.setText(String.valueOf(totalStudents));
    }

    private void handleGoToCategory(ActionEvent event, Category category) {
        final String PATH = "ExerciseAdminView.fxml";
        ExerciseStudentController controller = SceneLoader.loadSceneAndGetController(PATH, (MFXButton) event.getSource());
        if (controller != null) {
            controller.setInitialCategory(category);
        }
    }

private void setButtons() {
    viewAllExerciseBtn.setOnAction(this::handleViewAllExercise);
    findErrorBtn.setOnAction(event -> handleGoToCategory(event, Category.FIND_ERROR));
    outputBtn.setOnAction(event -> handleGoToCategory(event, Category.OUTPUT));
    completeCodeBtn.setOnAction(event -> handleGoToCategory(event, Category.SOLVE_CODE));
}

    private void updateText(Language lang) {
        pageText.setText(I18NUtils.getUI(FOLDER_UI, PAGE_TEXT, lang.getLocale()));
        userText.setText(I18NUtils.getUI(FOLDER_UI, USER_CARD, lang.getLocale()));
        exerciseTotalText.setText(I18NUtils.getUI(FOLDER_UI, EXERCISE_CARD, lang.getLocale()));
        exerciseSection.setText(I18NUtils.getUI(FOLDER_UI, EXERCISE_SECTION, lang.getLocale()));
        viewAllExerciseBtn.setText(I18NUtils.getUI(FOLDER_UI, VIEW_ALL, lang.getLocale()));
        findErrorBtn.setText(I18NUtils.getUI(FOLDER_UI, GO_TO_EXERCISE, lang.getLocale()));
        outputBtn.setText(I18NUtils.getUI(FOLDER_UI, GO_TO_EXERCISE, lang.getLocale()));
        completeCodeBtn.setText(I18NUtils.getUI(FOLDER_UI, GO_TO_EXERCISE, lang.getLocale()));

        categoryFindError.setText(I18NUtils.getEnum(CATEGORY_UI, CATEGORY_FIND, lang.getLocale()));
        descriptionFindError.setText(I18NUtils.getUI(FOLDER_UI, DESCRIPTION_FIND, lang.getLocale()));

        categoryOutput.setText(I18NUtils.getEnum(CATEGORY_UI, CATEGORY_OUTPUT, lang.getLocale()));
        descriptionOutput.setText(I18NUtils.getUI(FOLDER_UI, DESCRIPTION_OUTPUT, lang.getLocale()));

        categotyCompleteCode.setText(I18NUtils.getEnum(CATEGORY_UI, CATEGORY_COMPLETE_CODE, lang.getLocale()));
        descriptionCompleteCode.setText(I18NUtils.getUI(FOLDER_UI, DESCRIPTION_COMPLETE_CODE, lang.getLocale()));
    }
}
