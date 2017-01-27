package com.peleg.decider;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peleg.decider.com.peleg.decider.db.ItemsDBHelper;

import java.util.ArrayList;

/**
 * Created by Annie on 11/4/16.
 */
public class OptionsListFragment extends Fragment {

    private ArrayList<Choice> mItems;
    private RVOptionsAdapter mAdapter;
    private ItemsDBHelper mDbHelper;
    private RecyclerView rvItems;

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

        rvItems = (RecyclerView) view.findViewById(R.id.rvItems);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rvItems.addItemDecoration(decoration);

        mItems = OptionsList.getInstance().getList();
        mAdapter = new RVOptionsAdapter(getContext(),mItems);
        rvItems.setAdapter(mAdapter);

        rvItems.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        return view;
    }

    public void addNewItem(Choice item) {
        if(mAdapter!= null) {
            mAdapter.add(item,mItems.size());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mItems.size() == 0)
            loadItems();
        else
            refreshList();

    }

    private void refreshList() {
        mAdapter.notifyDataSetChanged();
    }

    private void loadItems() {
        mDbHelper = new ItemsDBHelper(getContext());
        Cursor cursor = mDbHelper.getItems();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Choice item = new Choice(cursor.getString(1), cursor.getFloat(2), cursor.getString(3));
                    mItems.add(item);
                    Log.e("Items Rank- ",String.valueOf(item.getRank()));
                } while (cursor.moveToNext());
            }

        }
    }

    @Override
    public void onDestroy() {
        if(mDbHelper!= null)
            mDbHelper.close();
        super.onDestroy();
    }
}
