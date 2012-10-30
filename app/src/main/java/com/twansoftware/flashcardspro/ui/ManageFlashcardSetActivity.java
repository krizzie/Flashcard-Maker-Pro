package com.twansoftware.flashcardspro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.ArrayList;

public class ManageFlashcardSetActivity extends RoboSherlockListActivity implements MenuItem.OnMenuItemClickListener, AdapterView.OnItemClickListener {
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
        getListView().setOnItemClickListener(this);
        getSupportActionBar().setTitle(flashcardSet.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        appData = stateManager.getAppData();
        final ArrayList<FlashcardSet> flashcardSets = appData.getFlashcardSets();
        flashcardSet = flashcardSets.get(flashcardSets.indexOf(flashcardSet));
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
            if (flashcardSet.getType() == FlashcardSet.Type.SINGLE_ANSWER) {
                sendToEditFlashcardScreen(new Flashcard("", ""));
            } else if (flashcardSet.getType() == FlashcardSet.Type.MULTIPLE_CHOICE) {
                sendToEditFlashcardScreen(new Flashcard("", 4));
            }
        } else if (menuItem.getTitle().equals(getResources().getString(R.string.quiz_flashcard_set_action_bar_text))) {
            // SEND TO PRINS ACTIVITY
        }
        return true;
    }

    private void sendToEditFlashcardScreen(final Flashcard flashcardToEdit) {
        final Intent intent = new Intent(this, ManageFlashcardActivity.class);
        intent.putExtra(ManageFlashcardActivity.FLASHCARD_TO_MANAGE_KEY, flashcardToEdit);
        intent.putExtra(ManageFlashcardActivity.FLASHCARD_PARENT_SET_KEY,  flashcardSet);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        final Flashcard selectedFlashcard = (Flashcard) adapterView.getItemAtPosition(position);
        sendToEditFlashcardScreen(selectedFlashcard);
    }
}
