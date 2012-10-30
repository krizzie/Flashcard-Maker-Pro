package com.twansoftware.flashcardspro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.twansoftware.flashcardspro.R;
import com.twansoftware.flashcardspro.entity.Flashcard;

import java.util.List;

public class FlashcardAdapter extends BaseAdapter {
    private List<Flashcard> flashcards;
    private final Context context;

    public FlashcardAdapter(final List<Flashcard> flashcards, final Context context) {
        this.flashcards = flashcards;
        this.context = context;
    }

    @Override
    public int getCount() {
        return flashcards.size();
    }

    @Override
    public Object getItem(final int i) {
        return flashcards.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return getItem(i).hashCode();
    }

    public void setFlashcards(final List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        final Flashcard selectedFlashcard = (Flashcard) getItem(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.flashcard_list_item, viewGroup, false);
        }
        final TextView flashcardQuestion = (TextView) view.findViewById(R.id.flashcard_list_item_question);
        flashcardQuestion.setText(selectedFlashcard.getQuestion());
        return view;
    }
}
