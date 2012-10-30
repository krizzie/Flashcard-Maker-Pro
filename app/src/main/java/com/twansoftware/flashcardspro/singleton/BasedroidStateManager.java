package com.twansoftware.flashcardspro.singleton;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twansoftware.flashcardspro.entity.Flashcard;
import com.twansoftware.flashcardspro.entity.FlashcardAppData;
import com.twansoftware.flashcardspro.entity.FlashcardSet;
import roboguice.util.Ln;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Author: achuinard
 * 4/5/12
 */
@Singleton
public class BasedroidStateManager {
    private static final String DEFAULT_NULL_STRING = "";
    private static final int DEFAULT_NULL_INTEGER = -999;
    private static final Gson gson = new Gson();

    private SharedPreferences sharedPreferences;

    @Inject
    public BasedroidStateManager(final SharedPreferences sharedPreferences) {
        Ln.d("Constructing BasedroidStateManager...");
        this.sharedPreferences = sharedPreferences;
    }

    private void saveString(final String key, final String value) {
        setStringPreference(key, value);
    }

    private boolean setStringPreference(final String key, final String value) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    private String loadString(final String key) {
        return sharedPreferences.getString(key, DEFAULT_NULL_STRING);
    }

    private void saveInteger(final String key, final Integer value) {
        setIntegerPreference(key, value);
    }

    private Integer loadInteger(final String key) {
        return sharedPreferences.getInt(key, DEFAULT_NULL_INTEGER);
    }

    private boolean setIntegerPreference(final String key, final Integer value) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    private void saveObject(final String key, final Serializable object) {
        final String json = gson.toJson(object);
        saveString(key, json);
    }

    private Object loadObject(final String key, final Class clazz) {
        final String json = loadString(key);
        Ln.d(json);
        return gson.fromJson(json, clazz);
    }

    private static final String FLASHCARD_APP_DATA_KEY = "flashcard_data";

    public FlashcardAppData getAppData() {
        if (sharedPreferences.contains(FLASHCARD_APP_DATA_KEY)) {
            return (FlashcardAppData) loadObject(FLASHCARD_APP_DATA_KEY, FlashcardAppData.class);
        } else {
            final FlashcardAppData appData = new FlashcardAppData(new ArrayList<FlashcardSet>());
            final FlashcardSet initialSet = new FlashcardSet("Sample Flashcard Set", FlashcardSet.Type.SINGLE_ANSWER);
            initialSet.getFlashcards().add(new Flashcard("What is the best flashcard app in Google Play?", "Flashcard Maker Pro"));
            appData.getFlashcardSets().add(initialSet);
            saveAppData(appData);
            return getAppData();
        }
    }

    public void saveAppData(final FlashcardAppData appData) {
        Collections.sort(appData.getFlashcardSets(), new Comparator<FlashcardSet>() {
            @Override
            public int compare(final FlashcardSet flashcardSet, final FlashcardSet flashcardSet1) {
                return flashcardSet.getDateCreated().compareTo(flashcardSet1.getDateCreated());
            }
        });
        for (final FlashcardSet flashcardSet : appData.getFlashcardSets()) {
            Collections.sort(flashcardSet.getFlashcards(), new Comparator<Flashcard>() {
                @Override
                public int compare(final Flashcard flashcard, final Flashcard flashcard1) {
                    return flashcard.getDateMade().compareTo(flashcard1.getDateMade());
                }
            });
        }
        saveObject(FLASHCARD_APP_DATA_KEY, appData);
    }
}
