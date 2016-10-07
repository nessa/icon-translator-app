package com.icon.nsales.icontranslate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nsales on 5/10/16.
 */
public class GridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private MainActivity context;

    public class VHHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.categories_spinner)
        Spinner spinner;

        public VHHeader(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public class VHItem extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        public ImageView icon;

        public VHItem(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public GridViewAdapter(Context context) {
        this.context = (MainActivity) context;
        this.context.calcTranslatedCategories();
    }

    @Override
    public int getItemCount() {
        return this.context.getPhrases().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public Phrase getItem(int position) {
        return this.context.getPhrases().get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return this.context.getPhrases().get(position - 1).getSortNumber();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grid_item, parent, false);
            return new VHItem(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grid_header, parent, false);
            return new VHHeader(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            VHItem itemHolder = (VHItem) holder;

            Phrase presentPhrase = getItem(position);

            itemHolder.icon.setTag(presentPhrase.getCode());
            itemHolder.icon.setImageResource(R.drawable.ic_build_black_24dp);

            itemHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.speakPhrase((String) view.getTag());
                }
            });

            itemHolder.icon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    context.showPhrase((String) view.getTag());
                    return true;
                }
            });
        } else {
            final VHHeader headerHolder = (VHHeader) holder;

            final ArrayAdapter<String> languagesArrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, context.getTranslatedCategories());
            languagesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            headerHolder.spinner.setAdapter(languagesArrayAdapter);
            headerHolder.spinner.setSelection(context.getLastCategoryIndex(), false);

            headerHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Log.d("GRID", "SELECT CATEGORY " + context.getCategories().get(position));
                    headerHolder.spinner.setSelection(position);
                    context.filterPhrases(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });


        }
    }
}
