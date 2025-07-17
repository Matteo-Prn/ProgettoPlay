package com.play.controllers.view;

import atlantafx.base.util.Animations;
import com.play.controllers.util.SceneLoader;
import com.play.controllers.util.SessionManager;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller per la schermata di caricamento iniziale.
 * Gestisce le animazioni di benvenuto e il passaggio alla homepage dello studente.
 */
public class LoadingController implements Initializable {

    @FXML
    private ImageView iconLogo;

    @FXML
    private MFXButton start;

    @FXML
    private Label welcome;

    @FXML
    private VBox contentBox;

    private Animation pulseAnimation;


    private static final String FOLDER_UI = "loading";
    private static final String WELCOME_MESSAGE = "welcome";
    private static final String START_BUTTON_TEXT = "start";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Impostazione binding per internazionalizzazione
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> {
                    welcome.setText(I18NUtils.getUI(FOLDER_UI, WELCOME_MESSAGE, newValue.getLocale()));
                    start.setText(I18NUtils.getUI(FOLDER_UI, START_BUTTON_TEXT, newValue.getLocale()));
                }
        );

        start.setOnAction(this::handleStartButton);

        // Imposta lo stato iniziale per l'animazione
        welcome.setVisible(false);
        start.setVisible(false);
        // Rimuove il VBox dal layout per non occupare spazio
        contentBox.setManaged(false);

        startAnimation();
    }




    private void startAnimation() {
        // Pausa iniziale per mostrare solo il logo
        PauseTransition initialPause = new PauseTransition(Duration.seconds(2));
        initialPause.setOnFinished(e -> {
            contentBox.setManaged(true); // Rende il VBox gestito dal layout
            welcome.setVisible(true);    // Rende la label visibile prima dell'animazione
        });

        // Animazione di "calcio"
        Animation kickAnimation = createKickAnimation();

        // Animazione di apparizione del bottone
        Animation buttonFadeIn = createButtonFadeInAnimation();

        // Esegue le animazioni in sequenza
        SequentialTransition sequentialTransition = new SequentialTransition(
                initialPause,
                kickAnimation,
                buttonFadeIn
        );
        sequentialTransition.play();
    }

    private Animation createKickAnimation() {
        // Crea l'animazione di slide per la label "Benvenuto"
        Animation zommIn = Animations.zoomIn(welcome, Duration.millis(3000));

        // Crea l'animazione di zoom-out per l'icona
        Animation zoomOut = Animations.zoomOut(iconLogo, Duration.millis(1800));
        zoomOut.setOnFinished(e -> iconLogo.setVisible(false));

        // Esegue le due animazioni contemporaneamente
        ParallelTransition parallelTransition = new ParallelTransition(zommIn, zoomOut);
        parallelTransition.setOnFinished(e -> start.setVisible(true)); // Mostra il bottone alla fine
        return parallelTransition;
    }

    private Animation createButtonFadeInAnimation() {
        // Crea l'animazione di fade-in per il bottone
        Animation fadeIn = Animations.fadeIn(start, Duration.millis(2500));

        // Crea l'animazione di pulse che si ripeterà all'infinito, con durata maggiore per rallentarla
        pulseAnimation = Animations.pulse(start, 1.02);
        pulseAnimation.setCycleCount(Animation.INDEFINITE);
        pulseAnimation.setRate(0.75);

        // Avvia l'animazione di pulse dopo che il fade-in è completato
        fadeIn.setOnFinished(e -> pulseAnimation.play());

        return fadeIn;
    }
    private void handleStartButton(Event event) {
        final String PATH = "HomepageStudentView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, start);
    }
}

