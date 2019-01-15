package com.util.cbba.caducitymeasure.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.util.cbba.caducitymeasure.R;
import com.util.cbba.caducitymeasure.persistence.entity.Item;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Item> mWords; // Cached copy of words

    public ItemListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mWords != null) {
            Item current = mWords.get(position);
            holder.wordItemView.setText(String.format("Item: %s Desc: %s Exp: %s",
                    current.getName(),
                    (TextUtils.isEmpty(current.getDescription()) ? " " : current.getDescription()),
                    current.getExpirationDate()));
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Items");
        }
    }

    public void setItems(List<Item> items){
        mWords = items;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }


    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }
}
