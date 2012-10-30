package com.twansoftware.flashcardspro.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.twansoftware.flashcardspro.R;
import com.twansoftware.flashcardspro.entity.Flashcard;
import com.twansoftware.flashcardspro.entity.FlashcardAnswer;
import com.twansoftware.flashcardspro.entity.FlashcardAppData;
import com.twansoftware.flashcardspro.entity.FlashcardSet;
import com.twansoftware.flashcardspro.singleton.BasedroidStateManager;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ManageFlashcardActivity extends RoboSherlockActivity implements MenuItem.OnMenuItemClickListener {
    public static final String FLASHCARD_TO_MANAGE_KEY = "flashcard_to_manage";
    public static final String FLASHCARD_PARENT_SET_KEY = "parent_flashcard_set";

    @Inject
    private BasedroidStateManager stateManager;

    @InjectExtra(FLASHCARD_TO_MANAGE_KEY)
    private Flashcard flashcardToEdit;

    @InjectExtra(FLASHCARD_PARENT_SET_KEY)
    private FlashcardSet parentFlashcardSet;

    @InjectView(R.id.manage_flashcard_question)
    private EditText flashcardQuestionEditText;

    @InjectView(R.id.manage_flashcard_answers_container)
    private LinearLayout flashcardAnswersContainer;

    private final List<EditText> answerEditTexts = new ArrayList<EditText>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_flashcard_activity);

        getSupportActionBar().setTitle(R.string.manage_flashcard_action_bar_title_text);
        flashcardQuestionEditText.setText(flashcardToEdit.getQuestion());
        setupAnswersView();
    }

    private void setupAnswersView() {
        if (parentFlashcardSet.getType() == FlashcardSet.Type.SINGLE_ANSWER) {
            final View answerView = LayoutInflater.from(this).inflate(R.layout.flashcard_answer_edit_text, flashcardAnswersContainer, true);
            final EditText singleAnswer = (EditText) answerView.findViewById(R.id.flashcard_answer_editable);
            singleAnswer.setText(flashcardToEdit.getAnswers().get(0).getAnswerText());
            singleAnswer.setHint(R.string.correct_answer_hint);
            answerEditTexts.add(singleAnswer);
        } else if (parentFlashcardSet.getType() == FlashcardSet.Type.MULTIPLE_CHOICE) {
            for (final FlashcardAnswer flashcardAnswer : flashcardToEdit.getAnswers()) {
                Ln.d("Appending answer box...");
                final View answerView = LayoutInflater.from(this).inflate(R.layout.flashcard_answer_edit_text, null, false);
                final EditText singleAnswer = (EditText) answerView.findViewById(R.id.flashcard_answer_editable);
                if (flashcardAnswer.getCorrectAnswer()) {
                    singleAnswer.setHint(R.string.correct_answer_hint);
                } else {
                    singleAnswer.setHint(R.string.incorrect_answer_hint);
                }
                singleAnswer.setText(flashcardAnswer.getAnswerText());
                answerEditTexts.add(singleAnswer);
                flashcardAnswersContainer.addView(answerView);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(R.string.save_flashcard_action_bar_text)
                .setOnMenuItemClickListener(this)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        final FlashcardAppData currentData = stateManager.getAppData();
        currentData.getFlashcardSets().remove(parentFlashcardSet);
        currentData.getFlashcardSets().add(parentFlashcardSet);
        parentFlashcardSet.getFlashcards().remove(flashcardToEdit);
        final String question = flashcardQuestionEditText.getText().toString();
        if (parentFlashcardSet.getType() == FlashcardSet.Type.SINGLE_ANSWER) {
            parentFlashcardSet.getFlashcards().add(new Flashcard(question, answerEditTexts.get(0).getText().toString()));
        } else if (parentFlashcardSet.getType() == FlashcardSet.Type.MULTIPLE_CHOICE) {
            final Flashcard newFlashcard = new Flashcard(question);
            for (final EditText answerEditText : answerEditTexts) {
                if (answerEditTexts.indexOf(answerEditText) == 0) { // correct answer is always first
                    newFlashcard.getAnswers().add(new FlashcardAnswer(answerEditText.getText().toString(), true));
                } else {
                    newFlashcard.getAnswers().add(new FlashcardAnswer(answerEditText.getText().toString(), false));
                }
            }
            parentFlashcardSet.getFlashcards().add(newFlashcard);
        }
        stateManager.saveAppData(currentData);
        finish();
        return true;
    }
}
