package com.peleg.decider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Annie on 11/4/16.
 */
public class RVItemsAdapter extends RecyclerView.Adapter<RVItemsAdapter.ViewHolder> {

    private List<ItemOption> mItems;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public RatingBar itemRank;
        public ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemRank = (RatingBar) itemView.findViewById(R.id.item_rank);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }

    public RVItemsAdapter(List<ItemOption> mItems, Context context) {
        this.mItems = mItems;
        this.mContext = context;
    }

    @Override
    public RVItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View itemView = layoutInflater.inflate(R.layout.item_option,parent,false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RVItemsAdapter.ViewHolder holder, int position) {
        ItemOption itemOption = mItems.get(position);

        TextView nameTextView = holder.itemName;
        nameTextView.setText(itemOption.getName());

        RatingBar ratingBar = holder.itemRank;
        ratingBar.setRating(itemOption.getRank());

        ImageView itemImageview = holder.itemImage;
        itemImageview.setImageBitmap(itemOption.getImage());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Context getContext() {
        return mContext;
    }
}
