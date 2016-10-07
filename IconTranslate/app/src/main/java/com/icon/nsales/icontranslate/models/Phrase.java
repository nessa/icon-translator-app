package com.icon.nsales.icontranslate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by nsales on 5/10/16.
 */
public class Phrase {

    private String mCode;
    private Integer mSortNumber;
    private String mCategory;

    public Phrase(String code, Integer sortNumber, String category) {
        this.mCode = code;
        this.mSortNumber = sortNumber;
        this.mCategory = category;
    }

    public Phrase(JSONObject o) {
        try {
            this.mCode = o.getString("code");
            this.mSortNumber = o.getInt("sort_number");
            this.mCategory = o.getString("category");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCode() {
        return mCode;
    }

    public Integer getSortNumber() {
        return mSortNumber;
    }

    public String getCategory() {
        return mCategory;
    }

}
