package com.icon.nsales.icontranslate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by nsales on 5/10/16.
 */
public class Phrase {

    private String mCode;
    private String mCategory;

    public Phrase(String code, String category) {
        this.mCode = code;
        this.mCategory = category;
    }

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
