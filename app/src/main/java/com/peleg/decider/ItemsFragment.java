package com.peleg.decider;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peleg.decider.com.peleg.decider.db.DbBitmapUtility;
import com.peleg.decider.com.peleg.decider.db.ItemsDBHelper;

import java.util.ArrayList;

/**
 * Created by Annie on 11/4/16.
 */
public class ItemsFragment extends Fragment {

    ArrayList<ItemOption> items;
    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    public ItemsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_recyclerview, container, false);

        RecyclerView rvItems = (RecyclerView) view.findViewById(R.id.rvItems);
        items = new ArrayList<>();
        loadItems();

        RVItemsAdapter adapter = new RVItemsAdapter(items,getContext());
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        return view;
    }

    private void loadItems() {
        ItemsDBHelper mDbHelper = new ItemsDBHelper(getContext());
        Cursor cursor = mDbHelper.getItems();
        if (cursor != null) {
            if( cursor.moveToFirst()) {
                do {
                    ItemOption item = new ItemOption(cursor.getString(1),cursor.getFloat(2), DbBitmapUtility.getImage(cursor.getBlob(3)));
                    items.add(item);
                } while (cursor.moveToNext());
            }
        }
    }

}