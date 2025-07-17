package com.play.controllers.util;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Utility class per gestire transizioni di fade su nodi JavaFX.
 */
public final class TransitionUtils {

    private TransitionUtils() {
    }

    /**
     * Applica una transizione di fade-in al nodo specificato.
     *
     * @param node il nodo su cui applicare la transizione
     * @param durationMillis la durata della transizione in millisecondi
     */
    public static void applyFadeIn(Node node, double durationMillis) {
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    /**
     * Applica una transizione di fade-out al nodo specificato.
     * Al termine della transizione viene eseguito il Runnable fornito.
     *
     * @param node il nodo su cui applicare la transizione
     * @param durationMillis la durata della transizione in millisecondi
     * @param onFinished azione da eseguire al termine della transizione
     */
    public static void applyFadeOut(Node node, double durationMillis, Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.millis(durationMillis), node);
        fade.setFromValue(0.75);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> onFinished.run());
        fade.play();
    }
}
