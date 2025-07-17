package com.play.controllers.component;

import com.play.controllers.util.SessionManager;
import com.play.model.enums.Language;
import com.play.util.provider.I18NUtils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller per il componente di selezione della lingua.
 * Gestisce una {@link MFXComboBox} per permettere all'utente di cambiare la lingua
 * dell'applicazione, sincronizzandosi con il {@link SessionManager}.
 */
public class ButtonLanguageController  implements Initializable {

    @FXML
    private MFXComboBox<Language> languageBox;

    private static final String SUBFOLDER = "language";
    private static final String FLOATING_TEXT = "floatingText";


/**
 * Inizializza il controller dopo il caricamento del file FXML.
 * Popola la ComboBox con le lingue disponibili, imposta il valore iniziale
 * e aggiunge i listener per gestire i cambiamenti di lingua.
 **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        languageBox.getItems().setAll(Language.values());
        languageBox.setConverter(createLanguageConverter());

        languageBox.setValue(SessionManager.getInstance().getCurrentLanguage());
        languageBox.setPromptText(createLanguageConverter().toString(
                SessionManager.getInstance().getCurrentLanguage()));

        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateLanguage(newValue));

        languageBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SessionManager.getInstance().setCurrentLanguage(newValue);
            }
        });


    }

    private void updateLanguage(Language language) {
        languageBox.setFloatingText(I18NUtils.getEnum(SUBFOLDER, FLOATING_TEXT, language.getLocale()));

        if (!languageBox.getItems().isEmpty()) {
            languageBox.setConverter(createLanguageConverter());
            languageBox.setValue(language);
        }
    }

    /**
     * Crea e restituisce un {@link StringConverter} per l'enum {@link Language}.
     * Questo converter è responsabile della traduzione dei valori dell'enum
     * nei loro nomi localizzati per la visualizzazione nella ComboBox.
     *
     * @return Un'istanza di {@link StringConverter} per la lingua.
     */
    private StringConverter<Language> createLanguageConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Language language) {
                if (language == null) {
                    return "";
                }
                Locale locale = SessionManager.getInstance().getCurrentLanguage().getLocale();
                return I18NUtils.getEnum(SUBFOLDER, language.getTextUIKey(), locale);
            }

            @Override
            public Language fromString(String string) {
                return null;
            }
        };
    }
}
