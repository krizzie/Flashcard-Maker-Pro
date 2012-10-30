package com.twansoftware.flashcardspro.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Flashcard implements Serializable {
    private final String question;
    private final ArrayList<String> answers;
    private final Date dateMade = new Date();

    public Flashcard(final String question) {
        this.question = question;
        this.answers = new ArrayList<String>();
    }

    public Flashcard(final String question, final String answer) {
        this(question);
        answers.add(answer);
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public Date getDateMade() {
        return dateMade;
    }
}
