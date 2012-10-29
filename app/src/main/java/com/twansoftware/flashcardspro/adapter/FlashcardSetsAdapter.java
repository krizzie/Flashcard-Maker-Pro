package com.twansoftware.flashcardspro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.twansoftware.flashcardspro.R;
import com.twansoftware.flashcardspro.entity.FlashcardSet;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FlashcardSetsAdapter extends BaseAdapter {
    private final List<FlashcardSet> flashcardSets;
    private final Context context;

    private static final Comparator<FlashcardSet> FLASHCARD_SET_REVERSE_CHRONOLOGICAL_COMPARATOR = new Comparator<FlashcardSet>() {
        @Override
        public int compare(final FlashcardSet flashcardSet, final FlashcardSet flashcardSet1) {
            return flashcardSet.getDateCreated().compareTo(flashcardSet1.getDateCreated());
        }
    };

    public FlashcardSetsAdapter(final List<FlashcardSet> flashcardSets, final Context context) {
        this.flashcardSets = flashcardSets;
        Collections.sort(flashcardSets, FLASHCARD_SET_REVERSE_CHRONOLOGICAL_COMPARATOR);
        this.context = context;
    }

    @Override
    public int getCount() {
        return flashcardSets.size();
    }

    @Override
    public Object getItem(final int i) {
        return flashcardSets.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        final FlashcardSet selectedFlashcardSet = (FlashcardSet) getItem(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.flashcard_set_list_item, viewGroup, false);
        }
        final TextView flashcardSetName = (TextView) view.findViewById(R.id.flashcard_set_list_item_flashcard_set_name);
        final TextView flashcardSetType = (TextView) view.findViewById(R.id.flashcard_set_list_item_flashcard_set_type);
        final TextView flashcardQuestionCount = (TextView) view.findViewById(R.id.flashcard_set_list_item_flashcard_question_count);
        flashcardSetName.setText(selectedFlashcardSet.getTitle());
        flashcardSetType.setText(selectedFlashcardSet.getType().toString());
        flashcardQuestionCount.setText(String.format("%dq", selectedFlashcardSet.getFlashcards().size()));
        return view;
    }
}
