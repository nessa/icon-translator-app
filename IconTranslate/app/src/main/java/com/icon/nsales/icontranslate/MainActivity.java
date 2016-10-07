package com.icon.nsales.icontranslate;

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

    private int lastCategoryIndex;

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

        lastCategoryIndex = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: Fill grid view


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
        languageItem.setTitle("EN");

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("MAIN", "ON PREPARE");

        return super.onPrepareOptionsMenu(menu);
    }

    private void updateMenuTitles() {
        Log.d("MAIN", "UPDATE");
        MenuItem languageItem = menu.findItem(R.id.language);
        languageItem.setTitle("ES");
    }

    public int getLastCategoryIndex() {
        return lastCategoryIndex;
    }

    public void filterPhrases(int categoryIndex) {
        if (categoryIndex != lastCategoryIndex) {
            Log.d("MAIN", "Filter by " + categories.get(categoryIndex));
            ArrayList<Phrase> phrases = DataManagement.getPhrases(categories.get(categoryIndex));

            gridViewAdapter.setPhrases(phrases);
            gridViewAdapter.notifyDataSetChanged();

            lastCategoryIndex = categoryIndex;
        }
    }

}
