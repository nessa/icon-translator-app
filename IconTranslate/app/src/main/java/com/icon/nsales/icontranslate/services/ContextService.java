package com.icon.nsales.icontranslate.services;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Locale;

/**
 * Created by nsales on 7/10/16.
 */
public class ContextService {

    private static String ICON_PREFIX = "icon_";
    private static String CATEGORY_PREFIX = "category_";
    private static String LANGUAGE_PREFIX = "language_";
    private static String PHRASE_PREFIX = "phrase_";


    public int getIconResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(ICON_PREFIX + aString, "drawable", packageName);
    }

    protected String getStringResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        try {
            return context.getString(resId);
        } catch (Resources.NotFoundException e) {
            Log.d("CONTEXT", "STRING NOT FOUND FOR "+aString);
            return "";
        }
    }

    @NonNull
    protected String getStringResourceByName(String aString, Context context, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));

        Context conf = context.createConfigurationContext(configuration);
        String packageName = conf.getPackageName();

        int resId = conf.getResources().getIdentifier(aString, "string", packageName);
        return conf.getResources().getString(resId);
    }

    public String getCategoryString(String category, Context context) {
        return getStringResourceByName(CATEGORY_PREFIX + category, context);
    }

    public String getLanguageString(String category, Context context) {
        return getStringResourceByName(LANGUAGE_PREFIX + category, context);
    }

    public String getPhraseString(String category, Context context) {
        return getStringResourceByName(PHRASE_PREFIX + category, context);
    }

    public String getPhraseStringByLanguage(String category, Context context, String locale) {
        return getStringResourceByName(PHRASE_PREFIX + category, context, locale);
    }
}
