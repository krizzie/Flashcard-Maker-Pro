package com.twansoftware.flashcardspro.ui;

import android.os.Bundle;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockListActivity;
import com.twansoftware.flashcardspro.adapter.FlashcardAdapter;
import com.twansoftware.flashcardspro.entity.FlashcardAppData;
import com.twansoftware.flashcardspro.entity.FlashcardSet;
import com.twansoftware.flashcardspro.singleton.BasedroidStateManager;
import roboguice.inject.InjectExtra;

import javax.inject.Inject;

public class ManageFlashcardSetActivity extends RoboSherlockListActivity {
    public static final String FLASHCARD_SET_EXTRA_KEY = "flashcard_set_extra_key";

    @Inject
    private BasedroidStateManager stateManager;

    private FlashcardAppData appData;

    @InjectExtra(value = FLASHCARD_SET_EXTRA_KEY)
    private FlashcardSet flashcardSet;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(flashcardSet.getTitle());
        setListAdapter(new FlashcardAdapter(flashcardSet.getFlashcards(), this));
    }
}
