package com.play.controllers.view;

import com.play.controllers.component.SidebarAdminController;
import com.play.controllers.util.SessionManager;
import com.play.dto.RankTable;
import com.play.model.enums.Category;
import com.play.model.enums.Language;
import com.play.service.ScoreService;
import com.play.service.StudentService;
import com.play.util.provider.I18NUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

/**
 * Controller per la pagina delle statistiche amministratore.
 * Gestisce la visualizzazione delle statistiche generali sugli utenti, esercizi e categorie.
 */
public class StatisticAdminController implements Initializable {

    @FXML
    private Label titlePage;

    @FXML
    private Label avarageUserText;

    @FXML
    private Label avarageUserValue;

    @FXML
    private Label categoryText;

    @FXML
    private Label categoryValue;

    @FXML
    private Label exerciseTotalText;

    @FXML
    private Label exerciseTotalValue;

    @FXML
    private Label userText;

    @FXML
    private Label userValue;

    @FXML
    private SidebarAdminController sidebarAdminController;

    private static final String FOLDER_UI = "stats";
    private static final String PAGE_TEXT = "titlePage";
    private static final String USER_CARD = "titleUserCard";
    private static final String AVARAGE_USER = "titleAvarageCard";
    private static final String EXERCISE_CARD = "titleExerciseCard";
    private static final String CATEGORY_CARD = "titleCategoryCard";
    private static final String ENUM_CATEGORY_FOLDER = "category";

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        sidebarAdminController.selectStaticsButton();
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        loadData();
        updateText(SessionManager.getInstance().getCurrentLanguage());
    }

    /**
     * Carica e aggiorna i dati delle statistiche nella UI.
     */
    private void loadData() {
        // Calcolo e imposto il numero totale di esercizi
        exerciseTotalValue.setText(String.valueOf(ScoreService.getTotalExerciseCount()));

        // Calcolo e imposto il numero totale di studenti
        int totalStudents = StudentService.getTotalStudentCount();
        userValue.setText(String.valueOf(totalStudents));

        // Calcolo e imposto la media dei punteggi
        if (totalStudents > 0) {
            List<RankTable> rankings = StudentService.getStudentRankings();
            double totalScore = rankings.stream().mapToDouble(RankTable::score).sum();
            double averageScore = totalScore / totalStudents;
            avarageUserValue.setText(String.format("%.2f", averageScore));
        } else {
            avarageUserValue.setText("0.00");
        }

        // Calcolo e imposto la categoria più giocata
        updateMostPlayedCategory(SessionManager.getInstance().getCurrentLanguage());
    }

    /**
     * Aggiorna i testi della UI in base alla lingua selezionata.
     * @param language Lingua corrente.
     */
    private void updateText(Language language) {
        titlePage.setText(I18NUtils.getUI(FOLDER_UI, PAGE_TEXT, language.getLocale()));
        userText.setText(I18NUtils.getUI(FOLDER_UI, USER_CARD, language.getLocale()));
        avarageUserText.setText(I18NUtils.getUI(FOLDER_UI, AVARAGE_USER, language.getLocale()));
        exerciseTotalText.setText(I18NUtils.getUI(FOLDER_UI, EXERCISE_CARD, language.getLocale()));
        categoryText.setText(I18NUtils.getUI(FOLDER_UI, CATEGORY_CARD, language.getLocale()));
        updateMostPlayedCategory(language);
    }

    /**
     * Aggiorna la categoria più giocata nella UI.
     * @param language Lingua corrente.
     */
    private void updateMostPlayedCategory(Language language) {
        Optional<Category> mostPlayedCategoryOpt = ScoreService.getMostPlayedCategory();
        if (mostPlayedCategoryOpt.isPresent()) {
            String categoryKey = mostPlayedCategoryOpt.get().textUIKey();
            categoryValue.setText(I18NUtils.getEnum(ENUM_CATEGORY_FOLDER, categoryKey, language.getLocale()));
        } else {
            categoryValue.setText("-");
        }
    }


}
