package com.twansoftware.flashcardspro.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class FlashcardAppData implements Serializable {
    private final ArrayList<FlashcardSet> flashcardSets;

    public FlashcardAppData(final ArrayList<FlashcardSet> flashcardSets) {
        this.flashcardSets = flashcardSets;
    }

    public ArrayList<FlashcardSet> getFlashcardSets() {
        return flashcardSets;
    }
}
