package com.twansoftware.flashcardspro.entity;

import java.io.Serializable;
import java.util.UUID;

public class FlashcardAnswer implements Serializable {
    private final UUID uuid = UUID.randomUUID();
    private final String answerText;
    private final Boolean correctAnswer;

    public FlashcardAnswer(final String answerText, final boolean correctAnswer) {
        this.answerText = answerText;
        this.correctAnswer = correctAnswer;
    }

    public String getAnswerText() {
        return answerText;
    }

    public Boolean getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FlashcardAnswer that = (FlashcardAnswer) o;
        return !(uuid != null ? !uuid.equals(that.uuid) : that.uuid != null);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
