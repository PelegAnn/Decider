package com.peleg.decider;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peleg.decider.com.peleg.decider.db.DbBitmapUtility;
import com.peleg.decider.com.peleg.decider.db.ItemsDBHelper;

import java.util.ArrayList;

/**
 * Created by Annie on 11/4/16.
 */
public class OptionsListFragment extends Fragment {

    private ArrayList<Choice> items;
    public static OptionsListFragment newInstance() {
        return new OptionsListFragment();
    }

    public OptionsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_recyclerview, container, false);

        RecyclerView rvItems = (RecyclerView) view.findViewById(R.id.rvItems);
        items = OptionsList.getInstance().getList();
        if(items.size() == 0)
            loadItems();
        else
            refreshList();
        RVOptionsAdapter adapter = new RVOptionsAdapter(getContext());
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new GridLayoutManager(getActivity(),2));

        return view;
    }

    private void refreshList() {
        // Refreshing list
    }

    private void loadItems() {
        ItemsDBHelper mDbHelper = new ItemsDBHelper(getContext());
        Cursor cursor = mDbHelper.getItems();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Choice item = new Choice(cursor.getString(1), cursor.getFloat(2), cursor.getString(3));
                    items.add(item);
                    Log.e("Items Rank- ",String.valueOf(item.getRank()));
                } while (cursor.moveToNext());
            }

        }
    }

}
