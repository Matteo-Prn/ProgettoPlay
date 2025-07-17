package com.play.controllers.view;

import com.play.controllers.util.SceneLoader;
import com.play.controllers.util.SessionManager;
import com.play.dao.user.StudentDAO;
import com.play.dto.user.StudentDto;
import com.play.exceptions.ApplicationException;
import com.play.model.entity.user.Student;
import com.play.model.enums.Language;
import com.play.service.StudentService;
import com.play.util.provider.I18NUtils;
import com.play.util.validation.ErrorCollector;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.play.exceptions.code.user.UserServiceErrorCode.ALREADY_EXISTS;
import static com.play.exceptions.code.user.UserValidationErrorCode.*;


/**
 * Controller per la pagina di registrazione utente.
 */
public class RegisterController implements Initializable {

    @FXML
    private VBox alertAccount;

    @FXML
    private Label alertMessageText;

    @FXML
    private Label alertTitleText;

    @FXML
    private MFXButton backLoginPageBtn;

    @FXML
    private VBox cardTitle;

    @FXML
    private Label cardTitleText;

    @FXML
    private Label errorFirstName;

    @FXML
    private Label errorLastName;

    @FXML
    private Label errorPassword;

    @FXML
    private Label errorUsername;

    @FXML
    private TextField fieldFirstName;

    @FXML
    private TextField fieldLastName;

    @FXML
    private MFXPasswordField fieldPassword;

    @FXML
    private TextField fieldUsername;

    @FXML
    private Label floatingFirstName;

    @FXML
    private Label floatingLastName;

    @FXML
    private Label floatingPassoword;

    @FXML
    private Label floatingUsername;

    @FXML
    private MFXButton registerBtn;

    @FXML
    private Label textRegister;

    private static final String SUBFOLDER_ERROR = "user";

    private static final String REGISTER_TITLE = "registerTitle";
    private static final String CARD_TITLE = "cardTitle";
    private static final String FLOATING_USERNAME = "floatingUsername";
    private static final String FLOATING_PASSWORD = "floatingPassword";
    private static final String FLOATING_FIRST_NAME = "floatingFirstName";
    private static final String FLOATING_LAST_NAME = "floatingLastName";
    private static final String BUTTON_REGISTER = "buttonRegister";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateText(SessionManager.getInstance().getCurrentLanguage());
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        hideAlert();
        hideError();
        setUpButtons();

        fieldUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            hideAlert();
            hideError();
            removeInvalidFieldStyles();
        });
        fieldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            hideAlert();
            hideError();
            removeInvalidFieldStyles();
        });
        fieldFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            hideAlert();
            hideError();
            removeInvalidFieldStyles();
        });
        fieldLastName.textProperty().addListener((observable, oldValue, newValue) -> {
            hideAlert();
            hideError();
            removeInvalidFieldStyles();
        });

    }


    /**
     * Gestisce la registrazione dell'utente, valida i dati e aggiorna la UI in caso di errori.
     * @param actionEvent Evento di azione generato dal click sul bottone di registrazione.
     */
    private void handleRegister(ActionEvent actionEvent) {
        hideAlert();
        hideError();

        StudentDto studentDto = new StudentDto(
                fieldUsername.getText(),
                fieldPassword.getText(),
                fieldFirstName.getText(),
                fieldLastName.getText(),
               SessionManager.getInstance().getCurrentLanguage()
        );

        ErrorCollector errorCollector = new ErrorCollector();
        boolean isLoginSuccessful = StudentService.registerStudent(studentDto, errorCollector);
        if (isLoginSuccessful) {
            Student student = new StudentDAO().findByUsername(studentDto.username());
            SessionManager.getInstance().setCurrentUser(student);
            SessionManager.getInstance().setCurrentLanguage(student.getLanguage());

            final String PATH = "Loading.fxml";
            SceneLoader.loadSceneAndGetController(PATH, registerBtn);
        } else if (errorCollector.hasErrors()) {
            for (ApplicationException error : errorCollector.getErrors()) {
                if (error.getErrorCode() == USERNAME_INVALID) {
                    errorUsername.setVisible(true);
                    fieldUsername.getStyleClass().add("invalid-field");
                    if (fieldUsername.getText().isEmpty()) {
                        errorUsername.setText(I18NUtils.getError(SUBFOLDER_ERROR, USERNAME_INVALID.getEmptyMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    } else {
                        errorUsername.setText(I18NUtils.getError(SUBFOLDER_ERROR, USERNAME_INVALID.getMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    }
                } else if (error.getErrorCode() == PASSWORD_INVALID) {
                    errorPassword.setVisible(true);
                    fieldPassword.getStyleClass().add("invalid-field");
                    if (fieldPassword.getText().isEmpty()) {
                        errorPassword.setText(I18NUtils.getError(SUBFOLDER_ERROR, PASSWORD_INVALID.getEmptyMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    } else {
                        errorPassword.setText(I18NUtils.getError(SUBFOLDER_ERROR, PASSWORD_INVALID.getMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    }
                } else if (error.getErrorCode() == FIRST_NAME_INVALID) {
                    errorFirstName.setVisible(true);
                    fieldFirstName.getStyleClass().add("invalid-field");
                    if (fieldFirstName.getText().isEmpty()) {
                        errorFirstName.setText(I18NUtils.getError(SUBFOLDER_ERROR, FIRST_NAME_INVALID.getEmptyMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    } else {
                        errorFirstName.setText(I18NUtils.getError(SUBFOLDER_ERROR, FIRST_NAME_INVALID.getMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    }
                } else if (error.getErrorCode() == LAST_NAME_INVALID) {
                    errorLastName.setVisible(true);
                    fieldLastName.getStyleClass().add("invalid-field");
                    if (fieldLastName.getText().isEmpty()) {
                        errorLastName.setText(I18NUtils.getError(SUBFOLDER_ERROR, LAST_NAME_INVALID.getEmptyMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    } else {
                        errorLastName.setText(I18NUtils.getError(SUBFOLDER_ERROR, LAST_NAME_INVALID.getMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    }
                } else if (error.getErrorCode() == ALREADY_EXISTS) {
                    showAlert();
                    alertTitleText.setText(I18NUtils.getError(SUBFOLDER_ERROR, ALREADY_EXISTS.getTitleMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                    alertMessageText.setText(I18NUtils.getError(SUBFOLDER_ERROR, ALREADY_EXISTS.getMessageKey(), SessionManager.getInstance().getCurrentLanguage().getLocale()));
                }
            }
        }
    }

    /**
     * Torna alla pagina di login.
     * @param actionEvent Evento di azione generato dal click sul bottone di ritorno.
     */
    private void toLoginPage(ActionEvent actionEvent) {
        SessionManager.getInstance().setCurrentLanguage(SessionManager.getInstance().getCurrentLanguage());
        final String PATH = "LoginView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, backLoginPageBtn);
    }


    private void setUpButtons() {
        backLoginPageBtn.setOnAction(this::toLoginPage);
        registerBtn.setOnAction(this::handleRegister);
    }

    private void hideAlert() {
        alertAccount.setVisible(false);
        alertAccount.setManaged(false);
        cardTitle.setVisible(true);
        cardTitle.setManaged(true);
    }
    private void showAlert() {
        alertAccount.setVisible(true);
        alertAccount.setManaged(true);
        cardTitle.setVisible(false);
        cardTitle.setManaged(false);
    }

    private void hideError() {
        errorUsername.setVisible(false);
        errorPassword.setVisible(false);
        errorFirstName.setVisible(false);
        errorLastName.setVisible(false);
    }
    private void showError() {
        errorUsername.setVisible(true);
        errorPassword.setVisible(true);
        errorFirstName.setVisible(true);
        errorLastName.setVisible(true);
    }

    /**
     * Rimuove lo stile di campo non valido dai campi di input.
     */
    private void removeInvalidFieldStyles() {
        fieldUsername.getStyleClass().remove("invalid-field");
        fieldPassword.getStyleClass().remove("invalid-field");
        fieldFirstName.getStyleClass().remove("invalid-field");
        fieldLastName.getStyleClass().remove("invalid-field");
    }


    /**
     * Aggiorna i testi della UI in base alla lingua selezionata.
     * @param lang Lingua corrente.
     */
    public void updateText(Language lang) {
        final String SUBFOLDER = "register";
        textRegister.setText(I18NUtils.getUI(SUBFOLDER, REGISTER_TITLE, lang.getLocale()));
        cardTitleText.setText(I18NUtils.getUI(SUBFOLDER, CARD_TITLE, lang.getLocale()));
        floatingUsername.setText(I18NUtils.getUI(SUBFOLDER, FLOATING_USERNAME, lang.getLocale()));
        floatingPassoword.setText(I18NUtils.getUI(SUBFOLDER, FLOATING_PASSWORD, lang.getLocale()));
        floatingFirstName.setText(I18NUtils.getUI(SUBFOLDER, FLOATING_FIRST_NAME, lang.getLocale()));
        floatingLastName.setText(I18NUtils.getUI(SUBFOLDER, FLOATING_LAST_NAME, lang.getLocale()));
        registerBtn.setText(I18NUtils.getUI(SUBFOLDER, BUTTON_REGISTER, lang.getLocale()));
    }
}
