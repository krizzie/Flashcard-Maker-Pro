package com.twansoftware.flashcardspro.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FlashcardSet implements Serializable {
    private final String id = UUID.randomUUID().toString();
    private final String title;
    private final ArrayList<Flashcard> flashcards;
    private final Date dateCreated = new Date();
    private final Type type;

    public FlashcardSet(final String title, final Type type) {
        this(title, new ArrayList<Flashcard>(), type);
    }

    public FlashcardSet(final String title, final ArrayList<Flashcard> flashcards, final Type type) {
        this.title = title;
        this.flashcards = flashcards;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        MULTIPLE_CHOICE("Multiple Choice"), SINGLE_ANSWER("Single Answer");

        private String displayName;

        Type(final String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FlashcardSet that = (FlashcardSet) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
