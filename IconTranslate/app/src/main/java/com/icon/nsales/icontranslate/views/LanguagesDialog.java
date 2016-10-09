package com.icon.nsales.icontranslate.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.icon.nsales.icontranslate.R;
import com.icon.nsales.icontranslate.adapters.LanguagesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Languages dialog class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * It contains a list with all languages to let the user select one or more.
 *
 * Layouts:
 * - dialog_languages.xml
 * - dialog_languages_item.xml
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
    public LanguagesDialog(final Context context) {
        super(context);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_languages);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        ButterKnife.bind(this);

        // Set list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        languagesList.setLayoutManager(mLayoutManager);

        adapter = new LanguagesListAdapter(context);
        languagesList.setAdapter(adapter);
    }

}