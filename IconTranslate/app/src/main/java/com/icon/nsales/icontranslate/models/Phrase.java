package com.icon.nsales.icontranslate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Recipe class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Class to contain all phrases's data.
 */
public class Phrase {

    private String mCode;
    private String mCategory;

    public Phrase(JSONObject o) {
        try {
            this.mCode = o.getString("code");
            this.mCategory = o.getString("category");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCode() {
        return mCode;
    }

    public String getCategory() {
        return mCategory;
    }

}
