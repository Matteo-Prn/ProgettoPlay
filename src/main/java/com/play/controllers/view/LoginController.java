package com.play.controllers.view;

import com.play.controllers.util.SceneLoader;
import com.play.controllers.util.SessionManager;
import com.play.dao.user.AdminDAO;
import com.play.dao.user.StudentDAO;
import com.play.dto.user.AdminDto;
import com.play.dto.user.StudentDto;
import com.play.exceptions.ApplicationException;
import com.play.model.entity.user.Admin;
import com.play.model.entity.user.Student;
import com.play.model.enums.Language;
import com.play.service.AdminService;
import com.play.service.StudentService;
import com.play.util.provider.I18NUtils;
import com.play.util.validation.ErrorCollector;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.play.exceptions.code.user.UserServiceErrorCode.*;

/**
 * Controller per la gestione della schermata di login.
 * Gestisce l'autenticazione di studenti e amministratori,
 * la visualizzazione degli errori e il cambio lingua.
 */
public class LoginController implements Initializable {

    @FXML
    private VBox alertPopup;

    @FXML
    private MFXPasswordField fieldPassword;

    @FXML
    private TextField fieldUsername;

    @FXML
    private Label floatingTextPassword;

    @FXML
    private Label floatingTextUsername;

    @FXML
    private MFXToggleButton isAdminToggle;

    @FXML
    private MFXButton loginAdminBtn;

    @FXML
    private MFXButton loginStudentBtn;

    @FXML
    private MFXButton registerBtn;

    @FXML
    private Label play;

    @FXML
    private Label subtitle;

    @FXML
    private Label textMessageAlert;

    @FXML
    private Label textTitleAlert;

    @FXML
    private Label textTitleCard;

    @FXML
    private HBox titleCard;

    @FXML
    private Label welcome;

    private static final String SUBFOLDER_ERROR = "user";

    private static final String WELCOME = "welcome";
    private static final String PLAY = "play";
    private static final String SUBTITLE = "subtitle";
    private static final String TITLE_CARD = "titleCard";
    private static final String TOGGLE = "toggle";
    private static final String FLOATING_USERNAME = "flotingUsername";
    private static final String PROMPT_USERNAME = "promptUsername";
    private static final String FLOATING_PASSWORD = "flotingPassword";
    private static final String PROMPT_PASSWORD = "promptPassword";
    private static final String LOGIN_STUDENT = "loginStudent";
    private static final String LOGIN_ADMIN = "loginAdmin";
    private static final String REGISTER = "register";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateText(newValue));
        updateText(SessionManager.getInstance().getCurrentLanguage());
        setUpButtons();
        hideAlert();
        showButtonLoginStudent();

        fieldUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            hideAlert();
            removeInvalidFieldStyles();
        });

        fieldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            hideAlert();
            removeInvalidFieldStyles();
        });


    }

    private void setUpButtons() {
        isAdminToggle.setOnAction(this::isAdminToggleOnAction);
        loginAdminBtn.setOnAction(this::handleLoginAdmin);
        loginStudentBtn.setOnAction(this::handleLoginStudent);
        registerBtn.setOnAction(this::toRegisterPage);
    }

    private void toRegisterPage(ActionEvent event) {
        final String PATH = "RegisterView.fxml";
        SceneLoader.loadSceneAndGetController(PATH, registerBtn);
    }

    /**
     * Gestisce il login dello studente, mostrando eventuali errori.
     *
     * @param event Evento di azione generato dal pulsante.
     */
    private void handleLoginStudent(ActionEvent event) {
        hideAlert();

        StudentDto studentDto = new StudentDto(
                fieldUsername.getText(),
                fieldPassword.getText(),
                null,
                null,
                null
        );

        ErrorCollector errorCollector = new ErrorCollector();
        boolean isLoginSuccessful = StudentService.loginStudent(studentDto, errorCollector);
        if (isLoginSuccessful) {
            Student student = new StudentDAO().findByUsername(studentDto.username());
            SessionManager.getInstance().setCurrentUser(student);
            SessionManager.getInstance().setCurrentLanguage(student.getLanguage());

            final String PATH = "HomepageStudentView.fxml";
            SceneLoader.loadSceneAndGetController(PATH, loginStudentBtn);
        } else {
            if(errorCollector.hasErrors()) {
                for(ApplicationException e : errorCollector.getErrors()) {
                    switch (e.getErrorCode()) {
                        case NOT_FOUND:
                            showAlert();
                            textTitleAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR, NOT_FOUND.getTitleMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            textMessageAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR, NOT_FOUND.getMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            break;

                        case INVALID_PASSWORD:
                            showAlert();
                            textTitleAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR, INVALID_PASSWORD.getTitleMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            textMessageAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR, INVALID_PASSWORD.getMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            fieldPassword.getStyleClass().add("invalid-field");
                            break;
                        default:
                    }
                }
            } else {
                fieldUsername.getStyleClass().add(".invalid-field");
                fieldPassword.getStyleClass().add(".invalid-field");
            }
        }
    }

    /**
     * Gestisce il login dell'amministratore, mostrando eventuali errori.
     *
     * @param event Evento di azione generato dal pulsante.
     */
    private void handleLoginAdmin(ActionEvent event) {
       hideAlert();

        AdminDto adminDto = new AdminDto(
                fieldUsername.getText(),
                fieldPassword.getText(),
                null,
                null,
                null
        );

        ErrorCollector errorCollector = new ErrorCollector();

        boolean isLoginSuccessful = AdminService.loginAdmin(adminDto, errorCollector);
        if (isLoginSuccessful) {
            Admin admin = new AdminDAO().findByUsername(adminDto.username());
            SessionManager.getInstance().setCurrentUser(admin);
            SessionManager.getInstance().setCurrentLanguage(admin.getLanguage());

            final String PATH = "HomepageAdminView.fxml";
            SceneLoader.loadSceneAndGetController(PATH, loginAdminBtn);
        } else {
            showAlert();
            if (errorCollector.hasErrors()) {
                for (ApplicationException e : errorCollector.getErrors()) {
                    switch (e.getErrorCode()) {
                        case NOT_AUTHORIZED:
                            textTitleAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR,
                                    NOT_AUTHORIZED.getTitleMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            textMessageAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR,
                                    NOT_AUTHORIZED.getMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            if (!fieldUsername.getText().isBlank() && !fieldPassword.getText().isBlank()) {
                                fieldUsername.getStyleClass().add("invalid-field");
                                fieldPassword.getStyleClass().add("invalid-field");
                            }
                            break;
                        case INVALID_PASSWORD:
                            textTitleAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR,
                                    INVALID_PASSWORD.getTitleMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            textMessageAlert.setText(I18NUtils.getError(SUBFOLDER_ERROR,
                                    INVALID_PASSWORD.getMessageKey(),
                                    SessionManager.getInstance().getCurrentLanguage().getLocale()));
                            fieldPassword.getStyleClass().add("invalid-field");
                            break;
                        default:
                    }
                }
            } else {
                showAlert();
            }
        }
    }

    /**
     * Gestisce il toggle tra login studente e login amministratore.
     *
     * @param event Evento di azione generato dal toggle.
     */
    private void isAdminToggleOnAction(ActionEvent event) {
        if (isAdminToggle.isSelected()) {
            showButtonLoginAdmin();
            resetFields();
        } else {
            showButtonLoginStudent();
            resetFields();
        }
    }

    private void showAlert() {
        alertPopup.setManaged(true);
        alertPopup.setVisible(true);
        titleCard.setManaged(false);
        titleCard.setVisible(false);
    }
    private void hideAlert() {
        alertPopup.setManaged(false);
        alertPopup.setVisible(false);
        titleCard.setManaged(true);
        titleCard.setVisible(true);
    }

    private void showButtonLoginAdmin() {
        loginAdminBtn.setManaged(true);
        loginAdminBtn.setVisible(true);
        loginStudentBtn.setManaged(false);
        loginStudentBtn.setVisible(false);
    }
    private void showButtonLoginStudent() {
        loginStudentBtn.setManaged(true);
        loginStudentBtn.setVisible(true);
        loginAdminBtn.setManaged(false);
        loginAdminBtn.setVisible(false);
    }

    private void resetFields() {
        fieldUsername.clear();
        fieldPassword.clear();
        removeInvalidFieldStyles();
    }

    private void removeInvalidFieldStyles() {
        fieldUsername.getStyleClass().remove("invalid-field");
        fieldPassword.getStyleClass().remove("invalid-field");
    }

    private void updateText(Language lang) {
        final String SUBFOLDER = "login";
        welcome.setText(I18NUtils.getUI(SUBFOLDER, WELCOME, lang.getLocale()));
        play.setText(I18NUtils.getUI(SUBFOLDER, PLAY, lang.getLocale()));
        subtitle.setText(I18NUtils.getUI(SUBFOLDER, SUBTITLE, lang.getLocale()));
        textTitleCard.setText(I18NUtils.getUI(SUBFOLDER, TITLE_CARD, lang.getLocale()));
        isAdminToggle.setText(I18NUtils.getUI(SUBFOLDER, TOGGLE, lang.getLocale()));
        floatingTextUsername.setText(I18NUtils.getUI(SUBFOLDER, FLOATING_USERNAME, lang.getLocale()));
        fieldUsername.setPromptText(I18NUtils.getUI(SUBFOLDER, PROMPT_USERNAME, lang.getLocale()));
        floatingTextPassword.setText(I18NUtils.getUI(SUBFOLDER, FLOATING_PASSWORD, lang.getLocale()));
        fieldPassword.setPromptText(I18NUtils.getUI(SUBFOLDER, PROMPT_PASSWORD, lang.getLocale()));
        loginStudentBtn.setText(I18NUtils.getUI(SUBFOLDER, LOGIN_STUDENT, lang.getLocale()));
        loginAdminBtn.setText(I18NUtils.getUI(SUBFOLDER, LOGIN_ADMIN, lang.getLocale()));
        registerBtn.setText(I18NUtils.getUI(SUBFOLDER, REGISTER, lang.getLocale()));
    }
}
