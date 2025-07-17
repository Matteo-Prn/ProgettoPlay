package com.play.controllers.util;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Classe per caricare scene in JavaFX.
 */
public final class SceneLoader {

    private SceneLoader() {
    }
    /**
     * Cambia la root della scena corrente, caricando un nuovo FXML (relativo a /fxml/)
     *
     * @param fxmlRelativePath es. "homepage.fxml" oppure "components/alert.fxml"
     * @param sourceNode       nodo qualsiasi della scena corrente (es. un Button)
     */
public static void loadScene(String fxmlRelativePath, Node sourceNode) {
    try {
        // Prima applica il fade out alla scena corrente
        Parent currentRoot = sourceNode.getScene().getRoot();

        FXMLLoader loader = new FXMLLoader(getFxmlUrl(fxmlRelativePath));
        Parent newRoot = loader.load();
        // Imposta l'opacità a 0 per il nuovo root
        newRoot.setOpacity(0);

        // Esegui il fade out e poi cambia la scena
        TransitionUtils.applyFadeOut(currentRoot, 150, () -> {
            // Cambia la root della scena
            sourceNode.getScene().setRoot(newRoot);
            // Esegui il fade in sulla nuova root
            TransitionUtils.applyFadeIn(newRoot, 250);
        });
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * Cambia la root della scena corrente, caricando un nuovo FXML (relativo a /fxml/)
     * e restituisce il controller del nuovo FXML.
     *
     * @param fxmlRelativePath es. "homepage.fxml" oppure "components/alert.fxml"
     * @param sourceNode       nodo qualsiasi della scena corrente (es. un Button)
     * @param <T>              tipo del controller
     * @return il controller del nuovo FXML
     */
public static <T> T loadSceneAndGetController(String fxmlRelativePath, Node sourceNode) {
    try {
        // Prima applica il fade out alla scena corrente
        Parent currentRoot = sourceNode.getScene().getRoot();

        FXMLLoader loader = new FXMLLoader(getFxmlUrl(fxmlRelativePath));
        Parent newRoot = loader.load();
        // Imposta l'opacità a 0 per il nuovo root
        newRoot.setOpacity(0);

        // Ottieni il controller
        T controller = loader.getController();

        // Esegui il fade out e poi cambia la scena
        TransitionUtils.applyFadeOut(currentRoot, 300, () -> {
            // Cambia la root della scena
            sourceNode.getScene().setRoot(newRoot);
            // Esegui il fade in sulla nuova root
            TransitionUtils.applyFadeIn(newRoot, 400);
        });

        return controller;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

    public static URL getFxml(String name) {
        String path = "/fxml/" + name + ".fxml";
        return SceneLoader.class.getResource(path);
    }


    // Utility per ottenere il percorso FXML
    private static URL getFxmlUrl(String relativePath) {
        return SceneLoader.class.getResource("/fxml/" + relativePath);
    }
}
