package com.icon.nsales.icontranslate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.icon.nsales.icontranslate.views.MainActivity;
import com.icon.nsales.icontranslate.models.Phrase;
import com.icon.nsales.icontranslate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Grid view cell adapter class.
 * Author: Noelia Sales Montes, noelia.salesmontes <at> gmail.com
 *
 * It declares the view of each grid view cells that contains an icon.
 *
 * Related layouts:
 * - VHHeader: grid_header.xml
 * - VHItem: grid_item.xml
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
        View view;
        @BindView(R.id.icon)
        public ImageView icon;

        public VHItem(View v) {
            super(v);
            ButterKnife.bind(this, v);
            view = v;
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
        return position - 1;
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

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder View holder
     * @param position Position of this view in the grid view
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHItem) {
            final VHItem itemHolder = (VHItem) holder;

            Phrase presentPhrase = getItem(position);

            itemHolder.icon.setImageResource(context.getIconResource(presentPhrase.getCode()));

            itemHolder.view.setTag(presentPhrase.getCode());

            itemHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setSelected(true);
                    context.speakPhrase((String) view.getTag());
                }
            });

            itemHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.setSelected(true);
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
                    headerHolder.spinner.setSelection(position);
                    context.filterPhrases(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });


        }
    }
}
