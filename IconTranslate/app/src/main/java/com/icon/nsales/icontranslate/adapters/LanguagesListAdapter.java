package com.icon.nsales.icontranslate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icon.nsales.icontranslate.views.MainActivity;
import com.icon.nsales.icontranslate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Languages list item adapter class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * It declares the view of each language list cells that contains a language name.
 *
 * Related layouts:
 * - ViewHolder: dialog_languages_item.xml
 */
public class LanguagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainActivity context;

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

    public LanguagesListAdapter(Context context) {
        this.context = (MainActivity) context;
        this.context.calcTranslatedLanguages();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.dialog_languages_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder itemHolder = (ViewHolder) holder;

        itemHolder.language.setText(getTranslatedLanguage(position));

        itemHolder.view.setTag(getLanguage(position));
        itemHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemHolder.view.setSelected(true);
                context.setLanguage((String) view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return context.getLanguages().size();
    }

    public String getLanguage(int position) {
        return context.getLanguages().get(position);
    }

    public String getTranslatedLanguage(int position) {
        return context.getTranslatedLanguages().get(position);
    }
}
