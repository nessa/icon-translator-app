package com.icon.nsales.icontranslate;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by nsales on 5/10/16.
 */
public class DataManagement {

    private static String CATEGORY_PREFIX = "category_";
    private static String LANGUAGE_PREFIX = "language_";
    private static String PHRASE_PREFIX = "phrase_";

    public static String CATEGORY_ALL = "all";

    private static ArrayList<Phrase> mPhrases = new ArrayList<>();
    private static ArrayList<String> mCategories = new ArrayList<>();


    public static String loadJSONFromAsset(String filename, Context context) {
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

    public static void loadPhrases(Context context) {

        mPhrases = new ArrayList<>();
        JSONArray list = null;

        try {
            list = new JSONArray(loadJSONFromAsset("phrases.json", context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (list != null) {
            mCategories = new ArrayList<>();
            mCategories.add("all");

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

    public static ArrayList<Phrase> getPhrases(String category) {

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


    public static ArrayList<String> getLanguages(Context context) {

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


    public static ArrayList<String> getCategories() {
        return mCategories;
    }


    public static int getIconResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(aString, "drawable", packageName);
    }

    protected static String getStringResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    @NonNull
    protected static String getStringResourceByName(String aString, Context context, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));

        Context conf = context.createConfigurationContext(configuration);
        String packageName = conf.getPackageName();

        int resId = conf.getResources().getIdentifier(aString, "string", packageName);
        return conf.getResources().getString(resId);
    }

    public static String getCategoryString(String category, Context context) {
        return getStringResourceByName(CATEGORY_PREFIX + category, context);
    }

    public static String getLanguageString(String category, Context context) {
        return getStringResourceByName(LANGUAGE_PREFIX + category, context);
    }

    public static String getPhraseString(String category, Context context) {
        return getStringResourceByName(PHRASE_PREFIX + category, context);
    }

    public static String getPhraseStringByLanguage(String category, Context context, String locale) {
        return getStringResourceByName(PHRASE_PREFIX + category, context, locale);
    }

}
