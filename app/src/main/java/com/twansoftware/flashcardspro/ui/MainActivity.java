package com.twansoftware.flashcardspro.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockListActivity;
import com.twansoftware.flashcardspro.R;
import com.twansoftware.flashcardspro.adapter.FlashcardSetsAdapter;
import com.twansoftware.flashcardspro.entity.FlashcardAppData;
import com.twansoftware.flashcardspro.entity.FlashcardSet;
import com.twansoftware.flashcardspro.singleton.BasedroidStateManager;

import javax.inject.Inject;

public class MainActivity extends RoboSherlockListActivity implements MenuItem.OnMenuItemClickListener, Spinner.OnItemSelectedListener, AdapterView.OnItemClickListener {

    @Inject
    private BasedroidStateManager stateManager;

    private FlashcardSet.Type flashcardDialogSetType = FlashcardSet.Type.SINGLE_ANSWER;

    private FlashcardAppData appData;

    private FlashcardSetsAdapter flashcardSetsAdapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appData = stateManager.getAppData();
        setListAdapter(flashcardSetsAdapter = new FlashcardSetsAdapter(appData.getFlashcardSets(), this));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(R.string.new_flashcard_set_action_bar_text)
                .setOnMenuItemClickListener(this)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        promptForNewFlashcardSet();
        return true;
    }

    private void promptForNewFlashcardSet() {
        final Context context = this;
        final View nameAndTypeView = LayoutInflater.from(context).inflate(R.layout.pick_set_name_and_type_dialog, null, false);
        final EditText enterNameText = (EditText) nameAndTypeView.findViewById(R.id.pick_name_and_type_name);
        final Spinner pickTypeSpinner = (Spinner) nameAndTypeView.findViewById(R.id.pick_name_and_type_type);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.flashcard_set_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickTypeSpinner.setAdapter(adapter);
        pickTypeSpinner.setOnItemSelectedListener(this);
        final DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int buttonClicked) {
                dialogInterface.dismiss();
                if (buttonClicked == DialogInterface.BUTTON_POSITIVE) {
                    appData.getFlashcardSets().add(0, new FlashcardSet(enterNameText.getText().toString(), flashcardDialogSetType));
                    stateManager.saveAppData(appData);
                    flashcardSetsAdapter.notifyDataSetChanged();
                }
            }
        };

        new AlertDialog.Builder(this)
                .setMessage("Enter flashcard set name and type.")
                .setView(nameAndTypeView)
                .setPositiveButton("Create", onClickListener)
                .setNegativeButton("Cancel", onClickListener)
                .setTitle("New Flashcard Set")
                .create().show();
    }

    @Override
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        if (position == 0) {
            flashcardDialogSetType = FlashcardSet.Type.SINGLE_ANSWER;
        } else if (position == 1) {
            flashcardDialogSetType = FlashcardSet.Type.MULTIPLE_CHOICE;
        }
    }

    @Override
    public void onNothingSelected(final AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        final FlashcardSet selectedFlashcardSet = (FlashcardSet) adapterView.getItemAtPosition(position);
        final Intent manageFlashcardSetIntent = new Intent(this, ManageFlashcardSetActivity.class);
        manageFlashcardSetIntent.putExtra(ManageFlashcardSetActivity.FLASHCARD_SET_EXTRA_KEY, selectedFlashcardSet);
        startActivity(manageFlashcardSetIntent);
    }
}
