package com.play.controllers.component;

import com.play.controllers.util.SceneLoader;
import com.play.controllers.util.SessionManager;
import com.play.model.enums.Language;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller della barra laterale per gli studenti.
 * Gestisce la navigazione tra le diverse sezioni dell'applicazione,
 */
public class SidebarStudentController implements Initializable {

    @FXML
    private MFXButton exerciseButton;

    @FXML
    private MFXButton homeButton;

    @FXML
    private Label languageSection;

    @FXML
    private MFXButton logoutButton;

    @FXML
    private MenuButton menuButton;

    @FXML
    private MFXButton rankingButton;

    @FXML
    private Label roleMenuButton;

    @FXML
    private MFXButton staticsButton;

    @FXML
    private Label usernameMenuButton;


    private static final String SUBFOLDER = "sidebar";
    private static final String ENUM = "role";

    private static final String HOME = "home";
    private static final String EXERCISES = "exercises";
    private static final String STATS = "stats";
    private static final String RANKING = "ranking";
    private static final String LANGUAGE_SECTION = "languageSection";
    private static final String LOGOUT = "logout";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());

        setUpButtons();
    }

    private void setUpButtons() {
        logoutButton.setOnAction(this::handleLogout);
        homeButton.setOnAction(this::handleHome);
        exerciseButton.setOnAction(this::handleExercise);
        rankingButton.setOnAction(this::handleRanking);
        staticsButton.setOnAction(this::handleStatics);
    }

    private void handleHome(Event event) {
        final String PATH = "HomepageStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, homeButton);
    }

    private void handleExercise(Event event) {
        final String PATH = "ExerciseStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, exerciseButton);
    }

    private void handleRanking(Event event) {
        final String PATH = "RankStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, rankingButton);
    }

    private void handleStatics(Event event) {
        final String PATH = "StatisticsStudent.fxml";
        SceneLoader.loadSceneAndGetController(PATH, staticsButton);
    }

    /**
     * Gestisce l'evento di logout.
     * Disconnette l'utente corrente e reindirizza alla schermata di login.
     */
    private void handleLogout(Event event) {
        try {
            SessionManager.getInstance().setCurrentLanguage(Language.IT);
            SessionManager.getInstance().logout();

            Stage stage = (Stage) menuButton.getScene().getWindow();

            URL loginUrl = getClass().getResource("/fxml/LoginView.fxml");
            FXMLLoader loader = new FXMLLoader(loginUrl);
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(loginScene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateText(Language lang) {
        homeButton.setText(I18NUtils.getUI(SUBFOLDER, HOME, lang.getLocale()));
        exerciseButton.setText(I18NUtils.getUI(SUBFOLDER, EXERCISES, lang.getLocale()));
        staticsButton.setText(I18NUtils.getUI(SUBFOLDER, STATS, lang.getLocale()));
        rankingButton.setText(I18NUtils.getUI(SUBFOLDER, RANKING, lang.getLocale()));
        languageSection.setText(I18NUtils.getUI(SUBFOLDER, LANGUAGE_SECTION, lang.getLocale()));
        logoutButton.setText(I18NUtils.getUI(SUBFOLDER, LOGOUT, lang.getLocale()));

        menuButton.setText(SessionManager.getInstance().getCurrentUser().getUsername());
        usernameMenuButton.setText(SessionManager.getInstance().getCurrentUser().getUsername());
        roleMenuButton.setText(I18NUtils.getEnum(ENUM, SessionManager.getInstance().getCurrentUser().getRole().getTextUIKey(), lang.getLocale()));
    }

    private void updateSelectedButton(MFXButton selectedButton) {
        homeButton.getStyleClass().remove("selected-button");
        exerciseButton.getStyleClass().remove("selected-button");
        staticsButton.getStyleClass().remove("selected-button");
        rankingButton.getStyleClass().remove("selected-button");

        setUpButtons();

        if (selectedButton != null) {
            selectedButton.getStyleClass().add("selected-button");
            selectedButton.setOnAction(null);
        }
    }

    public void selectHomeButton() {
        updateSelectedButton(homeButton);
    }

    public void selectExerciseButton() {
        updateSelectedButton(exerciseButton);
    }

    public void selectStaticsButton() {
        updateSelectedButton(staticsButton);
    }

    public void selectRankingButton() {
        updateSelectedButton(rankingButton);
    }


}
