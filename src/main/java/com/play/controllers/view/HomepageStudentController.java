package com.play.controllers.view;

import com.play.controllers.component.SidebarStudentController;
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
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller per la homepage dello studente.
 * Gestisce la visualizzazione delle statistiche, della classifica,
 * e la navigazione tra le categorie di esercizi.
 */
public class HomepageStudentController implements Initializable {

    @FXML
    private VBox sidebarStudent;

    @FXML
    private SidebarStudentController sidebarStudentController;

    @FXML
    private Label pageText;

    @FXML
    private Label titleStats;

    @FXML
    private Label exerciseUser;

    @FXML
    private Label exerciseTotal;

    @FXML
    private MFXProgressBar progessExercise;

    @FXML
    private Label titleRank;

    @FXML
    private Label userNameRank;

    @FXML
    private Label infoRank;

    @FXML
    private Label userPos;

    @FXML
    private Label onText;

    @FXML
    private Label totalPos;

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


    private static final String FOLDER_UI = "homepage-student";
    private static final String CATEGORY_UI = "category";

    private static final String PAGE_TEXT = "pageText";
    private static final String TITLE_STATS = "titleStats";
    private static final String TITLE_RANK = "titleRank";
    private static final String INFO_RANK = "infoRank";
    private static final String ON_TEXT = "onText";
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
        sidebarStudentController.selectHomeButton();
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());
        loadDashboardData();
        setButtons();
    }

    /**
     * Carica e aggiorna i dati della dashboard dello studente,
     * incluse statistiche sugli esercizi e posizione in classifica.
     */
    private void loadDashboardData() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            return; // o gestisci il caso in cui l'utente non sia loggato
        }

        // Esercizi
        int completedCount = ScoreService.countCompletedExercisesByUser(currentUser);
        List<ExerciseDto> allExercises = ExerciseService.getAllExercises();
        int totalCount = allExercises.size();

        exerciseUser.setText(String.valueOf(completedCount));
        exerciseTotal.setText(String.valueOf(totalCount));

        if (totalCount > 0) {
            progessExercise.setProgress((double) completedCount / totalCount);
        } else {
            progessExercise.setProgress(0.0);
        }

        // Classifica
        int rank = StudentService.getStudentRank(currentUser);
        long totalStudents = StudentService.getTotalStudentCount();

        userPos.setText(rank + "°");
        totalPos.setText(String.valueOf(totalStudents));
    }

    private void setButtons() {
        viewAllExerciseBtn.setOnAction(this::handleViewAllExercise);
        findErrorBtn.setOnAction(event -> handleGoToCategory(event, Category.FIND_ERROR));
        outputBtn.setOnAction(event -> handleGoToCategory(event, Category.OUTPUT));
        completeCodeBtn.setOnAction(event -> handleGoToCategory(event, Category.SOLVE_CODE));
    }

    private void handleViewAllExercise(Event event) {
        final String PATH = "ExerciseStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, viewAllExerciseBtn);
    }

    private void handleGoToCategory(ActionEvent event, Category category) {
        final String PATH = "ExerciseStudentView.fxml";
        ExerciseStudentController controller = SceneLoader.loadSceneAndGetController(PATH, (MFXButton) event.getSource());
        if (controller != null) {
            controller.setInitialCategory(category);
        }
    }


    private void updateText(Language lang) {
        pageText.setText(I18NUtils.getUI(FOLDER_UI, PAGE_TEXT, lang.getLocale()));
        titleStats.setText(I18NUtils.getUI(FOLDER_UI, TITLE_STATS, lang.getLocale()));
        titleRank.setText(I18NUtils.getUI(FOLDER_UI, TITLE_RANK, lang.getLocale()));
        infoRank.setText(I18NUtils.getUI(FOLDER_UI, INFO_RANK, lang.getLocale()));
        onText.setText(I18NUtils.getUI(FOLDER_UI, ON_TEXT, lang.getLocale()));
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

        userNameRank.setText(SessionManager.getInstance().getCurrentUser().getUsername());
    }
}

