package com.icon.nsales.icontranslate;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
        Log.d("DATA", "PHRASES COUNT "+ mPhrases.size());
        for(Phrase p: mPhrases) {
            Log.d("MAIN", "PHRASE "+p.getCode());
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
        Log.d("DATA", "PHRASES COUNT2 "+ mPhrases.size());
        for(Phrase p: mPhrases) {
            Log.d("MAIN", "PHRASE "+p.getCode());
        }
        return mCategories;
    }


    public static int getIconResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(aString, "drawable", packageName);
    }

    public static String getStringResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    public static String getStringResourceByName(String aString, Context context, String locale) {
        String packageName = context.getPackageName();

        Configuration conf = context.getResources().getConfiguration();
        conf.locale = new Locale(locale);
        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Resources resources = new Resources(context.getAssets(), metrics, conf);

        int resId = resources.getIdentifier(aString, "string", packageName);
        /* get localized string */
        return resources.getString(resId);
    }

    public static String getCategoryString(String category, Context context) {
        return getStringResourceByName(CATEGORY_PREFIX + category, context);
    }

    public static String getLanguageString(String category, Context context) {
        return getStringResourceByName(LANGUAGE_PREFIX + category, context);
    }

    public static String getPhraseString(String category, Context context) {
        return getStringResourceByName(LANGUAGE_PREFIX + category, context);
    }

}
