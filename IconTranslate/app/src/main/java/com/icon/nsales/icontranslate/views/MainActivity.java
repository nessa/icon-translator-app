package com.icon.nsales.icontranslate.views;

import android.content.SharedPreferences;
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

import com.icon.nsales.icontranslate.models.Phrase;
import com.icon.nsales.icontranslate.R;
import com.icon.nsales.icontranslate.adapters.GridViewAdapter;
import com.icon.nsales.icontranslate.app.MyApplication;
import com.icon.nsales.icontranslate.services.ContextService;
import com.icon.nsales.icontranslate.services.DataService;
import com.icon.nsales.icontranslate.services.TextToSpeechService;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static String PREFERENCES_LANGUAGE_KEY = "selected-language";

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
    SharedPreferences preferences;
    @Inject
    DataService dataService;
    @Inject
    ContextService contextService;
    @Inject
    TextToSpeechService ttsService;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getComponent().inject(this);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Get data
        dataService.loadPhrases(this);
        categories = dataService.getCategories();
        languages = dataService.getLanguages(this);

        selectedLanguage = preferences.getString(PREFERENCES_LANGUAGE_KEY, languages.get(0));
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

        ttsService.createOrUpdate(selectedLanguage);
    }

    @Override
    protected void onStop() {
        ttsService.stop();
        super.onStop();
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

    public ArrayList<String> getLanguages() {
        return this.languages;
    }

    public ArrayList<String> getTranslatedCategories() {
        return this.translatedCategories;
    }

    public ArrayList<String> getTranslatedLanguages() {
        return this.translatedLanguages;
    }

    public int getIconResource(String code) {
        return contextService.getIconResourceByName(code, this);
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

            SharedPreferences.Editor edit = preferences.edit();
            edit.clear();
            edit.putString(PREFERENCES_LANGUAGE_KEY, selectedLanguage);
            edit.apply();

            ttsService.createOrUpdate(selectedLanguage);

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
        ttsService.speak(contextService.getPhraseStringByLanguage(phraseCode, this, selectedLanguage));
    }

}
