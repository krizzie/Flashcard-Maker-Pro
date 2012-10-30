package com.twansoftware.flashcardspro.ui;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockListActivity;
import com.twansoftware.flashcardspro.R;
import com.twansoftware.flashcardspro.adapter.FlashcardAdapter;
import com.twansoftware.flashcardspro.entity.Flashcard;
import com.twansoftware.flashcardspro.entity.FlashcardAppData;
import com.twansoftware.flashcardspro.entity.FlashcardSet;
import com.twansoftware.flashcardspro.singleton.BasedroidStateManager;
import roboguice.inject.InjectExtra;

import javax.inject.Inject;

public class ManageFlashcardSetActivity extends RoboSherlockListActivity implements MenuItem.OnMenuItemClickListener {
    public static final String FLASHCARD_SET_EXTRA_KEY = "flashcard_set_extra_key";

    @Inject
    private BasedroidStateManager stateManager;

    private FlashcardAppData appData;

    @InjectExtra(value = FLASHCARD_SET_EXTRA_KEY)
    private FlashcardSet flashcardSet;

    private FlashcardAdapter flashcardAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appData = stateManager.getAppData();
        getSupportActionBar().setTitle(flashcardSet.getTitle());
        setListAdapter(flashcardAdapter = new FlashcardAdapter(flashcardSet.getFlashcards(), this));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(R.string.new_flashcard_action_bar_text)
                .setOnMenuItemClickListener(this)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(R.string.quiz_flashcard_set_action_bar_text)
                .setOnMenuItemClickListener(this)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        if (menuItem.getTitle().equals(getResources().getString(R.string.new_flashcard_action_bar_text))) {
            sendToNewFlashcardScreen();
        } else if (menuItem.getTitle().equals(getResources().getString(R.string.quiz_flashcard_set_action_bar_text))) {
            // SEND TO PRINS ACTIVITY
        }
        return true;
    }

    private void sendToNewFlashcardScreen() {
        final Intent intent = new Intent(this, ManageFlashcardActivity.class);
        final Flashcard value = new Flashcard("");
        flashcardSet.getFlashcards().add(0, value);
        flashcardAdapter.notifyDataSetChanged();
        updateAppDataFlashcardSet();
        intent.putExtra(ManageFlashcardActivity.FLASHCARD_TO_MANAGE_KEY, value);
        startActivity(intent);
    }

    private void updateAppDataFlashcardSet() {
        appData.getFlashcardSets().remove(flashcardSet);
        appData.getFlashcardSets().add(flashcardSet);
        stateManager.saveAppData(appData);
    }
}
