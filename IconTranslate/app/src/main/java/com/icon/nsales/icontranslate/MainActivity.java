package com.icon.nsales.icontranslate;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> categories;
    private ArrayList<String> languages;

    private GridViewAdapter gridViewAdapter;
    private LanguagesDialog languagesDialog;

    private String selectedLanguage;
    private int lastCategoryIndex;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.grid_view)
    RecyclerView gridView;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Get data
        DataManagement.loadPhrases(this);
        categories = DataManagement.getCategories();
        languages = DataManagement.getLanguages(this);

        selectedLanguage = languages.get(0);
        lastCategoryIndex = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? 4 : 1;
            }
        });
        gridView.setLayoutManager(mLayoutManager);

        ArrayList<Phrase> phrases = DataManagement.getPhrases(categories.get(lastCategoryIndex));
        gridViewAdapter = new GridViewAdapter(this, phrases, categories);
        gridView.setAdapter(gridViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MAIN", "ON CREATE");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // Create your menu...
        MenuItem languageItem = menu.findItem(R.id.language);
        languageItem.setTitle(selectedLanguage);

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.language:
                languagesDialog = new LanguagesDialog(this, languages);
                languagesDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public int getLastCategoryIndex() {
        return lastCategoryIndex;
    }


    public void setLanguage(String language) {
        if (!language.equals(selectedLanguage)) {
            selectedLanguage = language;

            MenuItem languageItem = menu.findItem(R.id.language);
            languageItem.setTitle(selectedLanguage);
        }

        if (languagesDialog != null) {
            languagesDialog.dismiss();
        }
    }

    public void filterPhrases(int categoryIndex) {
        if (categoryIndex != lastCategoryIndex) {
            ArrayList<Phrase> phrases = DataManagement.getPhrases(categories.get(categoryIndex));

            gridViewAdapter.setPhrases(phrases);
            gridViewAdapter.notifyDataSetChanged();

            lastCategoryIndex = categoryIndex;
        }
    }

    public void showPhrase(String phraseCode) {
        Snackbar.make(layout, DataManagement.getPhraseString(phraseCode, this),
            Snackbar.LENGTH_LONG)
            .show();
    }

    public void speakPhrase(String phraseCode) {
        Snackbar.make(layout, DataManagement.getPhraseStringByLanguage(phraseCode, this, selectedLanguage),
            Snackbar.LENGTH_LONG)
            .show();
    }

}
