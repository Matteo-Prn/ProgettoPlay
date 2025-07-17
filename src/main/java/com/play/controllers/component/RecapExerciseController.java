package com.play.controllers.component;

import com.play.controllers.util.ExerciseSession;
import com.play.controllers.util.SessionManager;
import com.play.dto.exercise.ExerciseDto;
import com.play.dto.exercise.InfoExerciseDto;
import com.play.dto.exercise.QuestionDto;
import com.play.factory.ExerciseFactory;
import com.play.model.entity.exercise.Answer;
import com.play.model.entity.exercise.Question;
import com.play.model.enums.Difficulty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller per il componente FXML che mostra un riepilogo di un esercizio.
 * Può essere usato in due contesti:
 * 1. Prima della creazione di un esercizio, per mostrare un'anteprima.
 * 2. Dopo aver completato un esercizio, per mostrare i risultati.
 */
public class RecapExerciseController implements Initializable {

    @FXML
    private VBox containerForumQuestion;
    @FXML
    private VBox cardStudentContainer;

    @FXML
    private CardExerciseStudentController cardExerciseStudentController;

    private InfoExerciseDto infoExerciseDto;
    private Difficulty exerciseDifficulty;
    private List<QuestionDto> questions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SessionManager.getInstance().currentLanguageProperty().addListener(
                (observable, oldValue, newValue) -> updateLanguageContent()
        );
    }


    /**
     * Popola la vista di riepilogo con i dati di un esercizio in fase di creazione.
     * Mostra le informazioni generali e le domande in modalità sola lettura.
     *
     * @param info       Le informazioni generali dell'esercizio.
     * @param difficulty La difficoltà dell'esercizio.
     * @param question   La lista delle domande dell'esercizio.
     */
    public void populate(InfoExerciseDto info, Difficulty difficulty, List<QuestionDto> question) {
        this.infoExerciseDto = info;
        this.exerciseDifficulty = difficulty;
        this.questions = question;

        updateLanguageContent();

        // Pulisce le domande precedenti e carica quelle nuove
        containerForumQuestion.getChildren().clear();
        for (QuestionDto q : this.questions) {
            if (q != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ForumQuestion.fxml"));
                    Node questionNode = loader.load();
                    ForumQuestionController controller = loader.getController();
                    controller.loadQuestionData(q);
                    controller.setReadOnly();
                    containerForumQuestion.getChildren().add(questionNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Popola la vista di riepilogo con i risultati di una sessione di gioco completata.
     * Mostra le domande con le risposte corrette e quelle date dall'utente evidenziate.
     *
     * @param session La sessione di gioco {@link ExerciseSession} conclusa.
     */
    public void populateWithResults(ExerciseSession session) {
        cardStudentContainer.setManaged(false);
        cardStudentContainer.setVisible(false);
        ExerciseDto exerciseDto = ExerciseFactory.toDto(session.getExercise());
        this.infoExerciseDto = exerciseDto.infoExercise();
        this.exerciseDifficulty = exerciseDto.difficulty();
        updateLanguageContent();

        containerForumQuestion.getChildren().clear();
        List<Question> questionList = session.getQuestions();
        Map<Question, Answer> userAnswers = session.getUserAnswers();

        for (Question q : questionList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ForumQuestion.fxml"));
                Node questionNode = loader.load();
                ForumQuestionController controller = loader.getController();
                Answer userAnswer = userAnswers.get(q);
                controller.loadRecapData(q, userAnswer); // Nuovo metodo per il recap
                containerForumQuestion.getChildren().add(questionNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void updateLanguageContent() {
        if (infoExerciseDto == null || exerciseDifficulty == null) {
            return;
        }
        ExerciseDto exerciseDto = new ExerciseDto(infoExerciseDto, exerciseDifficulty, questions);
        cardExerciseStudentController.setCardData(exerciseDto);
        cardExerciseStudentController.hidePlayAndScore();
    }
}
