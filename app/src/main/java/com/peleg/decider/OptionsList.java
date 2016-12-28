package com.peleg.decider;

import java.util.ArrayList;

/**
 * Created by hannypeleg on 12/27/16.
 */
public class OptionsList {
    private ArrayList<Choice> mItems;
    private static OptionsList mInstance;

    private OptionsList() {
        if(mItems == null) {
            mItems = new ArrayList<>();
        }
    }

    public static OptionsList getInstance() {
        if(mInstance == null)
            mInstance = new OptionsList();
        return mInstance;
    }

    public ArrayList<Choice> getList() {
        return mItems;
    }

}
