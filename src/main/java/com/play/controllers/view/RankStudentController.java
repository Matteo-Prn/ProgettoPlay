package com.play.controllers.view;

import com.play.controllers.component.SidebarStudentController;
import com.play.controllers.util.SessionManager;
import com.play.dto.RankTable;
import com.play.model.enums.Language;
import com.play.service.StudentService;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller per la pagina della classifica studente.
 */
public class RankStudentController implements Initializable {
    @FXML
    private Label scoreFirstRank;

    @FXML
    private Label scoreSecondRank;

    @FXML
    private Label scoreText1;

    @FXML
    private Label scoreText2;
    @FXML
    private Label scoreText3;

    @FXML
    private Label scoreThirdRank;

    @FXML
    private MFXTableView<RankTable> table;

    @FXML
    private Label titlePage;

    @FXML
    private Label usernameFirstRank;

    @FXML
    private Label usernameSecondRank;

    @FXML
    private Label usernameThirdRank;

    @FXML
    private VBox sidebarStudent;

    @FXML
    private SidebarStudentController sidebarStudentController;

    private MFXTableColumn<RankTable> rankColumn;
    private MFXTableColumn<RankTable> usernameColumn;
    private MFXTableColumn<RankTable> scoreColumn;

    private static final String FOLDER_UI = "rank";
    private static final String TITLE_PAGE_KEY = "titlePage";
    private static final String SCORE_TEXT = "scoreText";
    private static final String USERNAME_COLUMN_KEY = "usernameColumn";
    private static final String SCORE_COLUMN_KEY = "scoreColumn";
    private static final String RANK_COLUMN_KEY = "rankColumn";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidebarStudentController.selectRankingButton();
        setupTable();
        loadRankings();

        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());
    }

    /**
     * Configura la tabella dei ranking con le colonne e le relative factory.
     */
    private void setupTable() {
        rankColumn = new MFXTableColumn<>("", true, Comparator.comparing(RankTable::rank));
        usernameColumn = new MFXTableColumn<>("", true, Comparator.comparing(entry -> entry.student().getUsername()));
        scoreColumn = new MFXTableColumn<>("", true, Comparator.comparing(RankTable::score).reversed());

        rankColumn.setRowCellFactory(entry -> new MFXTableRowCell<>(RankTable::rank) {{
            setAlignment(Pos.CENTER);
        }});
        usernameColumn.setRowCellFactory(entry -> new MFXTableRowCell<>(rankEntry -> rankEntry.student().getUsername()));
        scoreColumn.setRowCellFactory(entry -> new MFXTableRowCell<>(RankTable::score) {{
            setAlignment(Pos.CENTER_RIGHT);
        }});

        table.getTableColumns().addAll(rankColumn, usernameColumn, scoreColumn);
        table.setFooterVisible(false);
        table.autosizeColumnsOnInitialization();
    }

    /**
     * Carica i dati dei ranking degli studenti e aggiorna il podio e la tabella.
     */
    private void loadRankings() {
        List<RankTable> rankings = StudentService.getStudentRankings();

        // Imposta i valori di default per il podio
        usernameFirstRank.setText("-");
        scoreFirstRank.setText("-");
        usernameSecondRank.setText("-");
        scoreSecondRank.setText("-");
        usernameThirdRank.setText("-");
        scoreThirdRank.setText("-");

        // Popola i podi se ci sono abbastanza utenti
        if (rankings.size() > 0) {
            populatePodium(usernameFirstRank, scoreFirstRank, rankings.get(0));
        }
        if (rankings.size() > 1) {
            populatePodium(usernameSecondRank, scoreSecondRank, rankings.get(1));
        }
        if (rankings.size() > 2) {
            populatePodium(usernameThirdRank, scoreThirdRank, rankings.get(2));
        }

        // Popola la tabella dal 4° posto in poi, altrimenti la svuota
        if (rankings.size() > 3) {
            table.setItems(FXCollections.observableArrayList(rankings.subList(3, rankings.size())));
        } else {
            table.setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * Aggiorna le label del podio con username e punteggio.
     * @param usernameLabel Label per l'username.
     * @param scoreLabel Label per il punteggio.
     * @param entry Dato del ranking.
     */
    private void populatePodium(Label usernameLabel, Label scoreLabel, RankTable entry) {
        usernameLabel.setText(entry.student().getUsername());
        scoreLabel.setText(String.valueOf(entry.score()));
    }

    /**
     * Aggiorna i testi della UI in base alla lingua selezionata.
     * @param lang Lingua corrente.
     */
    private void updateText(Language lang){
        titlePage.setText(I18NUtils.getUI(FOLDER_UI, TITLE_PAGE_KEY, lang.getLocale()));
        scoreText1.setText(I18NUtils.getUI(FOLDER_UI, SCORE_TEXT, lang.getLocale()));
        scoreText2.setText(I18NUtils.getUI(FOLDER_UI, SCORE_TEXT, lang.getLocale()));
        scoreText3.setText(I18NUtils.getUI(FOLDER_UI, SCORE_TEXT, lang.getLocale()));

        usernameColumn.setText(I18NUtils.getUI(FOLDER_UI, USERNAME_COLUMN_KEY, lang.getLocale()));
        scoreColumn.setText(I18NUtils.getUI(FOLDER_UI, SCORE_COLUMN_KEY, lang.getLocale()));
        rankColumn.setText(I18NUtils.getUI(FOLDER_UI, RANK_COLUMN_KEY, lang.getLocale()));

        table.autosizeColumns();
    }
}
