package com.icon.nsales.icontranslate;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by nsales on 5/10/16.
 */
public class DataService {

    public static String CATEGORY_ALL = "all";

    private ArrayList<Phrase> mPhrases = new ArrayList<>();
    private ArrayList<String> mCategories = new ArrayList<>();


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


    public ArrayList<String> getCategories() {
        return mCategories;
    }


}
