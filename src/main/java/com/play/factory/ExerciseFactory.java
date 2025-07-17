package com.play.factory;

import com.play.dto.exercise.AnswerDto;
import com.play.dto.exercise.ExerciseDto;
import com.play.dto.exercise.InfoExerciseDto;
import com.play.dto.exercise.QuestionDto;
import com.play.model.embedded.InfoExercise;
import com.play.model.entity.exercise.Answer;
import com.play.model.entity.exercise.Exercise;
import com.play.model.entity.exercise.Question;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory per la conversione tra DTO e entità di esercizi, domande e risposte.
 */
public final class ExerciseFactory {

    private ExerciseFactory() {}

    /**
     * Converte un {@link ExerciseDto} in un'entità {@link Exercise}.
     *
     * @param dto DTO dell'esercizio
     * @return entità {@link Exercise}
     */
    public static Exercise fromDto(ExerciseDto dto) {
        if (dto == null) {
            return null;
        }
        InfoExercise infoExercise = fromInfoDto(dto.infoExercise());
        Exercise.ExcerciseBuilder exerciseBuilder = new Exercise.ExcerciseBuilder()
                .setInfoExercise(infoExercise)
                .setDifficulty(dto.difficulty());

        for (QuestionDto questionDto : dto.questions()) {
            Question.QuestionBuilder questionBuilder = new Question.QuestionBuilder()
                    .setQuestionText(questionDto.questionText());
            for (AnswerDto answerDto : questionDto.answers()) {
                Answer answer = new Answer.AnswerBuilder()
                        .setText(answerDto.text())
                        .setCorrect(answerDto.isCorrect())
                        .build();
                questionBuilder.addAnswer(answer);
            }
            exerciseBuilder.addQuestion(questionBuilder.build());
        }
        return exerciseBuilder.build();
    }

    /**
     * Converte un {@link InfoExerciseDto} in un'entità {@link InfoExercise}.
     *
     * @param dto DTO delle info esercizio
     * @return entità {@link InfoExercise}
     */
    private static InfoExercise fromInfoDto(InfoExerciseDto dto) {
        if (dto == null) {
            return null;
        }
        return new InfoExercise.InfoExerciseBuilder()
                .setTitle(dto.title())
                .setDescription(dto.description())
                .setCategory(dto.category())
                .setTopic(dto.topic())
                .build();
    }

    /**
     * Converte un'entità {@link Exercise} in un {@link ExerciseDto}.
     *
     * @param exercise entità esercizio
     * @return DTO dell'esercizio
     */
    public static ExerciseDto toDto(Exercise exercise) {
        if (exercise == null) {
            return null;
        }
        InfoExerciseDto infoDto = toDto(exercise.getInfoExercise());
        List<QuestionDto> questionDtos = exercise.getQuestions().stream()
                .map(ExerciseFactory::toDto)
                .collect(Collectors.toList());
        return new ExerciseDto(infoDto, exercise.getDifficulty(), questionDtos);
    }

    /**
     * Converte un'entità {@link InfoExercise} in un {@link InfoExerciseDto}.
     *
     * @param infoExercise entità info esercizio
     * @return DTO delle info esercizio
     */
    public static InfoExerciseDto toDto(InfoExercise infoExercise) {
        if (infoExercise == null) {
            return null;
        }
        return new InfoExerciseDto(
                infoExercise.getTitle(),
                infoExercise.getDescription(),
                infoExercise.getCategory(),
                infoExercise.getTopic()
        );
    }

    /**
     * Converte un'entità {@link Question} in un {@link QuestionDto}.
     *
     * @param question entità domanda
     * @return DTO della domanda
     */
    public static QuestionDto toDto(Question question) {
        if (question == null) {
            return null;
        }
        List<AnswerDto> answerDtos = question.getAnswers().stream()
                .map(ExerciseFactory::toDto)
                .collect(Collectors.toList());
        return new QuestionDto(question.getQuestionText(), answerDtos);
    }

    /**
     * Converte un'entità {@link Answer} in un {@link AnswerDto}.
     *
     * @param answer entità risposta
     * @return DTO della risposta
     */
    public static AnswerDto toDto(Answer answer) {
        if (answer == null) {
            return null;
        }
        return new AnswerDto(answer.getText(), answer.isCorrect());
    }
}
