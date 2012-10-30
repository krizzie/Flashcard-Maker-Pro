package com.twansoftware.flashcardspro.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Flashcard implements Serializable {
    private final UUID uuid = UUID.randomUUID();
    private final String question;
    private final ArrayList<FlashcardAnswer> answers;
    private final Date dateMade = new Date();

    public Flashcard(final String question) {
        this.question = question;
        this.answers = new ArrayList<FlashcardAnswer>();
    }

    public Flashcard(final String question, final String answer) {
        this(question);
        answers.add(new FlashcardAnswer(answer, true));
    }

    public Flashcard(final String question, final int numberOfBlankAnswers) {
        this(question);
        for (int i = 0; i < numberOfBlankAnswers; i++) {
            answers.add(new FlashcardAnswer("", i == 0));
        }
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<FlashcardAnswer> getAnswers() {
        return answers;
    }

    public Date getDateMade() {
        return dateMade;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Flashcard flashcard = (Flashcard) o;
        return !(uuid != null ? !uuid.equals(flashcard.uuid) : flashcard.uuid != null);

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
