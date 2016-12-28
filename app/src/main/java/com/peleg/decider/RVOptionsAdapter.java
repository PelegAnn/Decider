package com.peleg.decider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Annie on 11/4/16.
 */
public class RVOptionsAdapter extends RecyclerView.Adapter<RVOptionsAdapter.ViewHolder> {

    private OptionsList opList;
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

    public RVOptionsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RVOptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View itemView = layoutInflater.inflate(R.layout.item_option,parent,false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(RVOptionsAdapter.ViewHolder holder, int position) {

        Choice choice = opList.getInstance().getList().get(position);

        TextView nameTextView = holder.itemName;
        nameTextView.setText(choice.getName());

        RatingBar ratingBar = holder.itemRank;
        ratingBar.setRating(choice.getRank());

        ImageView itemImageview = holder.itemImage;
        String path = choice.getImagePath();
        if(path != null)
            Picasso.with(getContext()).load(new File(path))
                    .fit().centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(itemImageview);

    }

    @Override
    public int getItemCount() {
        return opList.getInstance().getList().size();
    }

    public Context getContext() {
        return mContext;
    }
}
