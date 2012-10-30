package com.twansoftware.flashcardspro.ui;

import android.os.Bundle;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.twansoftware.flashcardspro.R;
import com.twansoftware.flashcardspro.entity.Flashcard;
import roboguice.inject.InjectExtra;

public class ManageFlashcardActivity extends RoboSherlockActivity {
    public static final String FLASHCARD_TO_MANAGE_KEY = "flashcard_to_manage";

    @InjectExtra(FLASHCARD_TO_MANAGE_KEY)
    private Flashcard flashcardToEdit;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.manage_flashcard_action_bar_title_text);
    }
}
