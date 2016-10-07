package com.icon.nsales.icontranslate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nsales on 6/10/16.
 */
public class LanguagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private MainActivity context;

    private ArrayList<String> languages;
    private ArrayList<String> translatedLanguages;


    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        @BindView(R.id.language_name)
        TextView language;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            view = v;
        }
    }

    public LanguagesListAdapter(Context context, ArrayList<String> languages) {
        this.context = (MainActivity) context;
        this.languages = languages;
        this.translatedLanguages = new ArrayList<>();

        for (int i = 0; i < this.languages.size(); i++) {
            this.translatedLanguages.add(DataManagement.getLanguageString(this.languages.get(i),
                this.context));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.dialog_languages_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;

        itemHolder.language.setText(getTranslatedLanguage(position));

        itemHolder.view.setTag(getLanguage(position));
        itemHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("LIST", "ON CLICK " + view.getTag());
                context.setLanguage((String) view.getTag());

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.languages.size();
    }

    public String getLanguage(int position) {
        return this.languages.get(position);
    }
    public String getTranslatedLanguage(int position) {
        return this.translatedLanguages.get(position);
    }
}
