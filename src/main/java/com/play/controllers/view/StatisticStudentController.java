package com.play.controllers.view;

import com.play.controllers.component.SidebarStudentController;
import com.play.controllers.util.SessionManager;
import com.play.model.entity.user.User;
import com.play.model.enums.Category;
import com.play.model.enums.Language;
import com.play.service.ExerciseService;
import com.play.service.ScoreService;
import com.play.service.StudentService;
import com.play.util.provider.I18NUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller per la pagina delle statistiche dello studente.
 * Gestisce la visualizzazione delle statistiche personali, grafici e ranking.
 */
public class StatisticStudentController implements Initializable {

    @FXML
    private Label avarageText;

    @FXML
    private Label avarageValue;

    @FXML
    private Label completedText;

    @FXML
    private Label completedValue;

    @FXML
    private PieChart pieChartStatistics;

    @FXML
    private Label rankText;

    @FXML
    private Label rankValue;

    @FXML
    private Label titleCard;

    @FXML
    private Label titlePage;

    @FXML
    private Label tobeCompletedText;

    @FXML
    private Label tobeCompletedValue;

    @FXML
    private StackedBarChart<String, Number> categoryScoreChart;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis scoreAxis;

    @FXML
    private VBox sidebarStudent;

    @FXML
    private SidebarStudentController sidebarStudentController;

    private static final String FOLDER_UI = "stats";
    private static final String TITLE_PAGE = "titlePage";
    private static final String TITLE_CARD_EXERCISE = "titleCardExercise";
    private static final String COMPLETED_TEXT = "completedText";
    private static final String TO_BE_COMPLETED_TEXT = "tobeCompletedText";
    private static final String TITLE_CARD_AVERAGE = "titleCardAvarage";
    private static final String TITLE_CARD_RANK = "titleCardRank";
    private static final String TITLE_CHART_EXERCISE = "titleChartExercise";


    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        sidebarStudentController.selectStaticsButton();
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        loadData();
        updateText(SessionManager.getInstance().getCurrentLanguage());
    }

    /**
     * Carica e aggiorna i dati delle statistiche dello studente nella UI.
     */
    private void loadData() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Gestisce il caso in cui non ci sia un utente loggato
            completedValue.setText("-");
            tobeCompletedValue.setText("-");
            avarageValue.setText("-");
            rankValue.setText("-"); // Da implementare la logica di ranking
            return;
        }

        // Esercizi completati e da completare
        int completedCount = ScoreService.countCompletedExercisesByUser(currentUser);
        int totalExercises = ExerciseService.getAllExercises().size();
        int toBeCompletedCount = totalExercises - completedCount;

        completedValue.setText(String.valueOf(completedCount));
        tobeCompletedValue.setText(String.valueOf(toBeCompletedCount));

        // Media punteggio
        int totalScore = ScoreService.getTotalScoreForUser(currentUser);
        double average = (completedCount > 0) ? (double) totalScore / completedCount : 0.0;
        avarageValue.setText(String.format("%.2f", average));

        // Dati per il PieChart
        loadPieChartData(completedCount, toBeCompletedCount);

        // Dati per il BarChart
        loadBarChartData(currentUser);

        int rank = StudentService.getStudentRank(currentUser);

        rankValue.setText(rank + "°");
    }

    /**
     * Carica i dati nel PieChart per esercizi completati e da completare.
     * @param completed Numero di esercizi completati.
     * @param notCompleted Numero di esercizi da completare.
     */
    private void loadPieChartData(int completed, int notCompleted) {
        Language lang = SessionManager.getInstance().getCurrentLanguage();
        String completedLabel = I18NUtils.getUI(FOLDER_UI, COMPLETED_TEXT, lang.getLocale());
        String notCompletedLabel = I18NUtils.getUI(FOLDER_UI, TO_BE_COMPLETED_TEXT, lang.getLocale());

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data(notCompletedLabel, notCompleted),
                        new PieChart.Data(completedLabel, completed));
        pieChartStatistics.setData(pieChartData);
    }

    /**
     * Carica i dati nel BarChart per i punteggi per categoria.
     * @param user Utente corrente.
     */
    private void loadBarChartData(User user) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Locale locale = SessionManager.getInstance().getCurrentLanguage().getLocale();

        Map<Category, Integer> scoresByCategory = ScoreService.getScoresForUser(user).stream()
                .collect(Collectors.groupingBy(
                        score -> score.getExercise().getInfoExercise().getCategory(),
                        Collectors.summingInt(score -> score.getScore())
                ));

        for (Category category : Category.values()) {
            String translatedCategory = I18NUtils.getEnum("category", category.textUIKey(), locale);
            int score = scoresByCategory.getOrDefault(category, 0);
            series.getData().add(new XYChart.Data<>(translatedCategory, score));
        }

        categoryScoreChart.getData().setAll(series);
        categoryScoreChart.setLegendVisible(false);

        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                Category category = Category.values()[i];
                Node barNode = data.getNode();
                if (barNode != null) {
                    barNode.getStyleClass().add("bar-category-" + category.name().toLowerCase().replace("_", "-"));
                }
            }
        });
    }

    /**
     * Aggiorna i testi della UI e i grafici in base alla lingua selezionata.
     * @param lang Lingua corrente.
     */
    private void updateText(Language lang) {
        titlePage.setText(I18NUtils.getUI(FOLDER_UI, TITLE_PAGE, lang.getLocale()));
        titleCard.setText(I18NUtils.getUI(FOLDER_UI, TITLE_CARD_EXERCISE, lang.getLocale()));
        completedText.setText(I18NUtils.getUI(FOLDER_UI, COMPLETED_TEXT, lang.getLocale()));
        tobeCompletedText.setText(I18NUtils.getUI(FOLDER_UI, TO_BE_COMPLETED_TEXT, lang.getLocale()));
        avarageText.setText(I18NUtils.getUI(FOLDER_UI, TITLE_CARD_AVERAGE, lang.getLocale()));
        rankText.setText(I18NUtils.getUI(FOLDER_UI, TITLE_CARD_RANK, lang.getLocale()));
        categoryScoreChart.setTitle(I18NUtils.getUI(FOLDER_UI, TITLE_CHART_EXERCISE, lang.getLocale()));

        // Ricarica i dati dei grafici per aggiornare le etichette tradotte
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            int completedCount = ScoreService.countCompletedExercisesByUser(currentUser);
            int totalExercises = ExerciseService.getAllExercises().size();
            loadPieChartData(completedCount, totalExercises - completedCount);
            loadBarChartData(currentUser);
        }
    }


}
