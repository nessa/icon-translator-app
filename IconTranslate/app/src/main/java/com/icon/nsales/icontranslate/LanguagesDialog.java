package com.icon.nsales.icontranslate;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nsales on 6/10/16.
 */
public class LanguagesDialog extends Dialog {

    // UI elements
    protected LanguagesListAdapter adapter;

    @BindView(R.id.languages_list)
    RecyclerView languagesList;

    /**
     * Dialog constructor
     *
     * @param context Application context
     */
    public LanguagesDialog(final Context context, ArrayList<String> languages) {
        super(context);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_languages);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        ButterKnife.bind(this);

        // Set list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        languagesList.setLayoutManager(mLayoutManager);

        adapter = new LanguagesListAdapter(context, languages);
        languagesList.setAdapter(adapter);
    }

}