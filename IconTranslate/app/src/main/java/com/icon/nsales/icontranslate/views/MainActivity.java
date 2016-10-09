package com.icon.nsales.icontranslate.views;

import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import com.icon.nsales.icontranslate.services.LocaleService;
import com.icon.nsales.icontranslate.services.TextToSpeechService;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * Main app activity.
 * It has:
 * - A menu item that indicates the selected language. It shows a dialog to change it.
 * - A menu item that launch an intent to download selected language package.
 * - A grid view with the content.
 *
 * Related layouts:
 * - Menu: main_menu.xml
 * - Content: activity_main.xml
 */
public class MainActivity extends AppCompatActivity {

    private static String PREFERENCES_LANGUAGE_KEY = "selected-language";

    // Data
    private ArrayList<Phrase> phrases;
    private ArrayList<String> categories;
    private ArrayList<String> languages;
    private ArrayList<String> translatedCategories;
    private ArrayList<String> translatedLanguages;

    // Views and adapters
    private Menu menu;
    private GridViewAdapter gridViewAdapter;
    private LanguagesDialog languagesDialog;

    // Behaviour variables
    private String selectedLanguage;
    private int lastCategoryIndex;

    // Butter knife injected views
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout layout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.grid_view)
    RecyclerView gridView;

    // Dagger injected services
    @Inject
    SharedPreferences preferences;
    @Inject
    DataService dataService;
    @Inject
    ContextService contextService;
    @Inject
    TextToSpeechService ttsService;
    @Inject
    LocaleService localeService;


    // LIFECYCLE METHODS

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState Data supplied when the activity is being re-initialized
     *                           after previously being shut down.
     */
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

    /**
     * Called when the user resumes the activity from the Paused state.
     */
    @Override
    protected void onResume() {
        super.onResume();

        ttsService.createOrUpdate(selectedLanguage);
        phrases = dataService.getPhrases(categories.get(lastCategoryIndex));

        populateViewForOrientation();
    }

    /**
     * Called when your activity is still partially visible, but most often is an indication
     * that the user is leaving the activity and it will soon enter the Stopped state.
     * It's useful to stop services in this method to prevent any resources to affect battery
     * life while your activity is paused and the user does not need them.
     */
    @Override
    protected void onPause() {
        super.onPause();

        ttsService.stop();
    }


    /**
     * Called when the configuration changes (like orientation).
     * @param newConfig The new configuration.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        populateViewForOrientation();
    }

    /**
     *  Inflate the menu items to use them in the action bar
     *  @param menu The menu to inflate with options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem languageItem = menu.findItem(R.id.language);
        languageItem.setTitle(selectedLanguage);

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called when a menu item is selected.
     * @param item Selected menu item
     * @return A boolean that indicates if an action takes effect.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.language:
                languagesDialog = new LanguagesDialog(this);
                languagesDialog.show();
                return true;
            case R.id.downloadLanguagePackage:
                ttsService.downloadLanguagePackages(selectedLanguage,
                        localeService.getLocaleCountryFromCode(selectedLanguage), layout,
                        String.format(getString(R.string.language_package_installed), selectedLanguage));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called from onResume or onConfiguration change. It reloads the UX, in this case, the
     * grid view.
     */
    public void populateViewForOrientation() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_columns));
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? getResources().getInteger(R.integer.grid_columns) : 1;
            }
        });
        gridView.setLayoutManager(mLayoutManager);

        gridViewAdapter = new GridViewAdapter(this);
        gridView.setAdapter(gridViewAdapter);
    }

    // GETTERS

    /**
     * Get last selected category index inside categories array.
     * @return Last selected category index.
     */
    public int getLastCategoryIndex() {
        return lastCategoryIndex;
    }

    /**
     * Get phrases array.
     * @return Phrases array.
     */
    public ArrayList<Phrase> getPhrases() {
        return this.phrases;
    }

    /**
     * Get untranslated languages array.
     * @return Languages array.
     */
    public ArrayList<String> getLanguages() {
        return this.languages;
    }

    /**
     * Get untranslated categories array.
     * @return Categories array.
     */
    public ArrayList<String> getTranslatedCategories() {
        return this.translatedCategories;
    }

    /**
     * Get translated languages array.
     * @return Translated languages array.
     */
    public ArrayList<String> getTranslatedLanguages() {
        return this.translatedLanguages;
    }

    /**
     * Get an icon given a phrase code.
     * @param code Phrase code.
     * @return Resource int icon.
     */
    public int getIconResource(String code) {
        return contextService.getIconResourceByName(code, this);
    }


    // CALC

    /**
     * Set a new array with all translated string obtained from languages array.
     */
    public void calcTranslatedLanguages() {
        this.translatedLanguages = new ArrayList<>();

        for (int i = 0; i < this.languages.size(); i++) {
            this.translatedLanguages.add(contextService.getLanguageString(this.languages.get(i),
                this));
        }
    }

    /**
     * Set a new array with all translated categories obtained from languages array.
     */
    public void calcTranslatedCategories() {
        this.translatedCategories = new ArrayList<>();

        for (int i = 0; i < this.categories.size(); i++) {
            this.translatedCategories.add(contextService.getCategoryString(this.categories.get(i),
                this));
        }
    }


    // SETTERS

    /**
     * Set a new language and update it on shared preferences and reload tts service.
     * @param language New selected language
     */
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


    // FUNCTIONALITY

    /**
     * Filter phrases by its category
     * @param categoryIndex Index of the selected category in the categories array.
     */
    public void filterPhrases(int categoryIndex) {
        if (categoryIndex != lastCategoryIndex) {
            phrases = dataService.getPhrases(categories.get(categoryIndex));
            gridViewAdapter.notifyDataSetChanged();

            lastCategoryIndex = categoryIndex;
        }
    }

    /**
     * Show a message to the user with the phrase in the locale (system) language.
     * @param phraseCode Code of the phrase to show
     */
    public void showPhrase(String phraseCode) {
        Snackbar.make(layout, contextService.getPhraseString(phraseCode, this),
            Snackbar.LENGTH_LONG)
            .show();
    }

    /**
     * Translate the phrase to voice in the selected language.
     * @param phraseCode Code of the phrase to translate to voice
     */
    public void speakPhrase(String phraseCode) {
        if (ttsService.isReady()) {
            ttsService.speak(contextService.getPhraseStringByLanguage(phraseCode, this,
                    selectedLanguage));
        } else {
            Snackbar.make(layout, getString(R.string.tts_service_not_ready),
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}
