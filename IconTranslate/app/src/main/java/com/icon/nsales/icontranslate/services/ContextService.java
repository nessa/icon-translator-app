package com.icon.nsales.icontranslate.services;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Locale;

/**
 * Context service class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Needed to get translations and resources of elements in a different language from
 * present locale.
 */
public class ContextService {

    private static String ICON_PREFIX = "icon_";
    private static String CATEGORY_PREFIX = "category_";
    private static String LANGUAGE_PREFIX = "language_";
    private static String PHRASE_PREFIX = "phrase_";


    /**
     * Get icon resource for a given code. The icon name will be "CATEGORY_PREFIX + code".
     * @param aString Code
     * @param context Context to get the resources
     * @return Resource int
     */
    public int getIconResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(ICON_PREFIX + aString, "drawable", packageName);
    }

    /**
     * Get translated string for a given key in the present locale.
     * @param aString Key
     * @param context Context to get the resources
     * @return Translated string
     */
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

    /**
     * Get translated string for a given key in a given locale.
     * @param aString Key
     * @param context Context to get the resources
     * @param locale Locale code
     * @return Translated string
     */
    @NonNull
    protected String getStringResourceByName(String aString, Context context, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));

        Context conf = context.createConfigurationContext(configuration);
        String packageName = conf.getPackageName();

        int resId = conf.getResources().getIdentifier(aString, "string", packageName);
        return conf.getResources().getString(resId);
    }

    /**
     * Get a translated category string for a given key in the present locale.
     * @param category Category code
     * @param context Context to get the resources
     * @return Translated category string
     */
    public String getCategoryString(String category, Context context) {
        return getStringResourceByName(CATEGORY_PREFIX + category, context);
    }

    /**
     * Get a translated language string for a given key in the present locale.
     * @param language Language code
     * @param context Context to get the resources
     * @return Translated language string
     */
    public String getLanguageString(String language, Context context) {
        return getStringResourceByName(LANGUAGE_PREFIX + language, context);
    }

    /**
     * Get a translated phrase string for a given key in the present locale.
     * @param phrase Phrase code
     * @param context Context to get the resources
     * @return Translated phrase string
     */
    public String getPhraseString(String phrase, Context context) {
        return getStringResourceByName(PHRASE_PREFIX + phrase, context);
    }

    /**
     * Get a translated phrase string for a given key in a given locale.
     * @param phrase Phrase code
     * @param context Context to get the resources
     * @param locale Locale code
     * @return Translated phrase string
     */
    public String getPhraseStringByLanguage(String phrase, Context context, String locale) {
        return getStringResourceByName(PHRASE_PREFIX + phrase, context, locale);
    }
}
