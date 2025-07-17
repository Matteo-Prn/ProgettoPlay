package com.play.controllers.component;

import com.play.controllers.util.IconUtils;
import com.play.controllers.util.NotificationType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller per la visualizzazione delle notifiche.
 * Questa classe gestisce l'inizializzazione e l'aggiornamento delle notifiche
 */
public class NotificationController implements Initializable {

    @FXML
    private FontIcon icon;

    @FXML
    private Label message;

    @FXML
    private Label title;

    @FXML
    private VBox wrapperIcon;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Imposta il messaggio della notifica.
     * @param message Il testo del messaggio da visualizzare.
     */
    public void setMessage(String message) {
        this.message.setText(message);
    }

    /**
     * Imposta il titolo della notifica.
     * @param title Il testo del titolo da visualizzare.
     */
    public void setTitle(String title) {
        this.title.setText(title);
    }

    /**
     * Imposta l'icona e lo stile della notifica in base al tipo.
     * @param type Il tipo di notifica (ERROR, WARNING, INFO, SUCCESS).
     */
    public void setIcon(NotificationType type) {
        // Imposta l'icona corretta
        icon.setIconCode(IconUtils.getIconForNotification(type));

        // Rimuove le classi di stile precedenti per evitare conflitti
        wrapperIcon.getStyleClass().removeIf(style -> style.startsWith("wrapper-background-color-"));
        icon.getStyleClass().removeIf(style -> style.startsWith("icon-"));

        // Aggiunge le nuove classi di stile in base al tipo di notifica
        switch (type) {
            case ERROR:
                wrapperIcon.getStyleClass().add("wrapper-background-color-red");
                icon.getStyleClass().add("icon-error");
                break;
            case WARNING:
                wrapperIcon.getStyleClass().add("wrapper-background-color-yellow");
                icon.getStyleClass().add("icon-warning");
                break;
            case INFO:
                wrapperIcon.getStyleClass().add("wrapper-background-color-blue");
                icon.getStyleClass().add("icon-info");
                break;
            case SUCCESS:
                wrapperIcon.getStyleClass().add("wrapper-background-color-green");
                icon.getStyleClass().add("icon-success");
                break;
        }
    }

    /**
     * Aggiorna il testo del titolo e del messaggio in base alla lingua corrente.
     */
    public void updateText() {
        // TODO: Implementare la logica di traduzione usando SessionManager e I18NUtils.
    }
}
