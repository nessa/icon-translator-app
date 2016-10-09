package com.icon.nsales.icontranslate.services;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Locale service class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Needed to get country the associated to a given language code.
 */
public class LocaleService {

    // Locale data
    private ArrayList<Pair<String, String>> LOCALE_COUNTRIES = new ArrayList<>(Arrays.asList(
            new Pair<>("es", "ES"),
            new Pair<>("en", "US"),
            new Pair<>("fr", "FR")
    ));


    /**
     * Get locale country for a given code
     * @param code Language code
     * @return String for locale country
     */
    public String getLocaleCountryFromCode(String code) {
        for (int i = 0; i < LOCALE_COUNTRIES.size(); i++) {
            if (LOCALE_COUNTRIES.get(i).first.equals(code.toLowerCase())) {
                return LOCALE_COUNTRIES.get(i).second;
            }
        }

        return "";
    }

}
