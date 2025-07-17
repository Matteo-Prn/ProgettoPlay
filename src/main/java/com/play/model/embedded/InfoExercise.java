package com.play.model.embedded;

import com.play.model.enums.Category;
import com.play.model.enums.Topic;
import jakarta.persistence.*;

/**
 * Classe che incapsula le informazioni generali di un esercizio,
 * come titolo, descrizione, categoria e argomento.
 */
@Embeddable
public class InfoExercise {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "topic", nullable = false)
    @Enumerated(EnumType.STRING)
    private Topic topic;

    /**
     * Costruttore protetto per JPA.
     */
    protected InfoExercise() {
    }

    /**
     * Costruttore che utilizza l'InfoExerciseBuilder.
     * @param builder il builder per le informazioni dell'esercizio
     */
    public InfoExercise(InfoExerciseBuilder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.category = builder.category;
        this.topic = builder.topic;
    }

    /**
     * @return il titolo dell'esercizio
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return la descrizione dell'esercizio
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return la categoria dell'esercizio
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @return l'argomento dell'esercizio
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * Confronta due InfoExercise per uguaglianza.
     * @param o l'oggetto da confrontare
     * @return true se sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfoExercise that)) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (category != that.category) return false;
        return topic == that.topic;
    }

    /**
     * Calcola l'hash code per le informazioni dell'esercizio.
     * @return l'hash code
     */
    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (topic != null ? topic.hashCode() : 0);
        return result;
    }

    /**
     * Builder per la creazione di oggetti InfoExercise.
     */
    public static final class InfoExerciseBuilder {
        private String title;
        private String description;
        private Category category;
        private Topic topic;

        /**
         * Imposta il titolo dell'esercizio.
         * @param title il titolo
         * @return il builder
         */
        public InfoExerciseBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Imposta la descrizione dell'esercizio.
         * @param description la descrizione
         * @return il builder
         */
        public InfoExerciseBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Imposta la categoria dell'esercizio.
         * @param category la categoria
         * @return il builder
         */
        public InfoExerciseBuilder setCategory(Category category) {
            this.category = category;
            return this;
        }

        /**
         * Imposta l'argomento dell'esercizio.
         * @param topic l'argomento
         * @return il builder
         */
        public InfoExerciseBuilder setTopic(Topic topic) {
            this.topic = topic;
            return this;
        }

        /**
         * Costruisce l'oggetto InfoExercise.
         * @return le informazioni dell'esercizio
         */
        public InfoExercise build() {
            return new InfoExercise(this);
        }
    }
}
