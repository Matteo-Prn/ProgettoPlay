package com.play.dto.exercise;

import com.play.model.enums.Category;
import com.play.model.enums.Difficulty;
import com.play.model.enums.Topic;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) per deserializzare gli esercizi dal file JSON di default.
 * Utilizza chiavi i18n per titolo e descrizione.
 */
public class JsonExerciseDto {
    public String titleKey;
    public String descriptionKey;
    public String category;
    public String difficulty;
    public String topic;
    public List<JsonQuestionDto> questions;

    /**
     * Converte questo DTO (da JSON) in un DTO dell'applicazione ({@link ExerciseDto}).
     * Le chiavi 'titleKey' e 'descriptionKey' vengono passate direttamente
     * come 'title' e 'description' nel nuovo DTO, senza traduzione.
     *
     * @return Un'istanza di {@link ExerciseDto}.
     */
    public ExerciseDto toExerciseDto() {
        InfoExerciseDto infoDto = new InfoExerciseDto(
                this.titleKey,
                this.descriptionKey,
                Category.valueOf(this.category.toUpperCase()),
                Topic.valueOf(this.topic.toUpperCase())
        );

        List<QuestionDto> questionDtos = this.questions.stream()
                .map(JsonQuestionDto::toQuestionDto)
                .collect(Collectors.toList());

        return new ExerciseDto(
                infoDto,
                Difficulty.valueOf(this.difficulty.toUpperCase()),
                questionDtos
        );
    }

    /**
     * Data Transfer Object (DTO) interno per le domande lette da JSON.
     *
     * @param questionText Testo della domanda.
     * @param answers      Lista delle risposte possibili.
     */
    public static class JsonQuestionDto {
        public String questionText;
        public List<JsonAnswerDto> answers;

        /**
         * Converte questo DTO in un {@link QuestionDto}.
         *
         * @return Un'istanza di {@link QuestionDto}.
         */
        public QuestionDto toQuestionDto() {
            List<AnswerDto> answerDtos = this.answers.stream()
                    .map(JsonAnswerDto::toAnswerDto)
                    .collect(Collectors.toList());
            return new QuestionDto(this.questionText, answerDtos);
        }
    }

    /**
     * Data Transfer Object (DTO) interno per le risposte lette da JSON.
     *
     * @param text      Testo della risposta.
     * @param isCorrect Indica se la risposta è corretta.
     */
    public static class JsonAnswerDto {
        public String text;
        public boolean isCorrect;

        /**
         * Converte questo DTO in un {@link AnswerDto}.
         *
         * @return Un'istanza di {@link AnswerDto}.
         */
        public AnswerDto toAnswerDto() {
            return new AnswerDto(this.text, this.isCorrect);
        }
    }
}