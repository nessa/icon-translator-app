package com.icon.nsales.icontranslate;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Phrase> phrases;
    private ArrayList<String> categories;
    private ArrayList<String> languages;

    private ArrayList<String> translatedCategories;
    private ArrayList<String> translatedLanguages;

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

    @Inject
    DataService dataService;
    @Inject
    ContextService contextService;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getComponent().inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Get data
        dataService.loadPhrases(this);
        categories = dataService.getCategories();
        languages = dataService.getLanguages(this);

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

        phrases = dataService.getPhrases(categories.get(lastCategoryIndex));
        gridViewAdapter = new GridViewAdapter(this);
        gridView.setAdapter(gridViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    public ArrayList<Phrase> getPhrases() {
        return this.phrases;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public ArrayList<String> getLanguages() {
        return this.languages;
    }

    public ArrayList<String> getTranslatedCategories() {
        return this.translatedCategories;
    }

    public ArrayList<String> getTranslatedLanguages() {
        return this.translatedLanguages;
    }

    public void calcTranslatedLanguages() {
        this.translatedLanguages = new ArrayList<>();

        for (int i = 0; i < this.languages.size(); i++) {
            this.translatedLanguages.add(contextService.getLanguageString(this.languages.get(i),
                this));
        }
    }

    public void calcTranslatedCategories() {
        this.translatedCategories = new ArrayList<>();

        for (int i = 0; i < this.categories.size(); i++) {
            this.translatedCategories.add(contextService.getCategoryString(this.categories.get(i),
                this));
        }
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
            phrases = dataService.getPhrases(categories.get(categoryIndex));
            gridViewAdapter.notifyDataSetChanged();

            lastCategoryIndex = categoryIndex;
        }
    }

    public void showPhrase(String phraseCode) {
        Snackbar.make(layout, contextService.getPhraseString(phraseCode, this),
            Snackbar.LENGTH_LONG)
            .show();
    }

    public void speakPhrase(String phraseCode) {
        // TODO: Use TTS to speak
        Snackbar.make(layout, contextService.getPhraseStringByLanguage(phraseCode, this, selectedLanguage),
            Snackbar.LENGTH_LONG)
            .show();
    }

}
