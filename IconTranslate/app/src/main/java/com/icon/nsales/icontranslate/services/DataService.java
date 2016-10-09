package com.icon.nsales.icontranslate.services;

import android.content.Context;

import com.icon.nsales.icontranslate.models.Phrase;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Data service class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Needed to load data from JSON files.
 */
public class DataService {

    public static String CATEGORY_ALL = "all";

    private ArrayList<Phrase> mPhrases = new ArrayList<>();
    private ArrayList<String> mCategories = new ArrayList<>();


    /**
     * Load data from a given JSON file.
     * @param filename File asset with the stored data
     * @param context Context to get assets
     * @return Data string
     */
    public String loadJSONFromAsset(String filename, Context context) {
        String json;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    /**
     * Load phrases from JSON and stores them in an array. Also it fills the categories array.
     * @param context Context to get assets
     */
    public void loadPhrases(Context context) {

        mPhrases = new ArrayList<>();
        JSONArray list = null;

        try {
            list = new JSONArray(loadJSONFromAsset("phrases.json", context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (list != null) {
            mCategories = new ArrayList<>();
            mCategories.add(CATEGORY_ALL);

            for (int i = 0; i < list.length(); i++) {
                try {
                    Phrase p = new Phrase(list.getJSONObject(i));

                    if (!mCategories.contains(p.getCategory())) {
                        mCategories.add(p.getCategory());
                    }

                    mPhrases.add(p);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get phrases with a given category. If category is CATEGORY_ALL, then return all categories.
     * @param category Category code
     * @return Phrases array
     */
    public ArrayList<Phrase> getPhrases(String category) {

        if (category.equals(CATEGORY_ALL)) {
            return mPhrases;
        } else {
            ArrayList<Phrase> phrases = new ArrayList<>();

            for (Phrase p: mPhrases) {
                if (p.getCategory().equals(category)) {
                    phrases.add(p);
                }
            }

            return phrases;
        }
    }

    /**
     * Load languages from JSON and stores them in an array.
     * @param context Context to get assets
     * @return Languages array
     */
    public ArrayList<String> getLanguages(Context context) {

        ArrayList<String> languages = new ArrayList<>();
        JSONArray list = null;

        try {
            list = new JSONArray(loadJSONFromAsset("languages.json", context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                try {
                    languages.add(list.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return languages;
    }

    /**
     * Get categories loaded from phrases in loadPhrases method.
     * @return Categories array.
     */
    public ArrayList<String> getCategories() {
        return mCategories;
    }
}
