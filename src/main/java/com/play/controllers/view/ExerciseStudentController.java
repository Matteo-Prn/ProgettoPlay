package com.play.controllers.view;

import com.play.controllers.component.CardExerciseStudentController;
import com.play.controllers.component.SidebarStudentController;
import com.play.controllers.util.SessionManager;
import com.play.dto.exercise.ExerciseDto;
import com.play.model.enums.Category;
import com.play.model.enums.Language;
import com.play.service.ExerciseService;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller per la vista degli esercizi disponibili per lo studente.
 * Permette di filtrare gli esercizi per categoria e visualizzare le card degli esercizi.
 */
public class ExerciseStudentController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private SidebarStudentController sidebarStudentController;

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

    private List<ExerciseDto> allExercises;
    private MFXButton selectedButton;
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

    private static final Logger LOG = Logger.getLogger(ExerciseStudentController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidebarStudentController.selectExerciseButton();
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());

        loadAllExercises();
        setupFilterButtons();
        selectButton(allCategoriesBtn);
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
     * Imposta la categoria iniziale selezionata e filtra gli esercizi di conseguenza.
     *
     * @param category Categoria da selezionare inizialmente.
     */
    public void setInitialCategory(Category category) {
        if (category != null) {
            filterExercisesByCategory(category);
            switch (category) {
                case FIND_ERROR:
                    selectButton(category1Btn);
                    break;
                case OUTPUT:
                    selectButton(category2Btn);
                    break;
                case SOLVE_CODE:
                    selectButton(category3Btn);
                    break;
            }
        }
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CardExerciseStudent.fxml"));
                    Node card = loader.load();
                    CardExerciseStudentController controller = loader.getController();
                    controller.setCardData(exercise);
                    cardContainer.getChildren().add(card);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Impossibile caricare la card dell'esercizio", e);
                }
            }
        }
    }

    /**
     * Seleziona il pulsante passato come attivo, rimuovendo lo stile dal precedente.
     *
     * @param button Pulsante da selezionare.
     */
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

