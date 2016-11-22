package com.peleg.decider;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Annie on 11/3/16.
 */
public class ItemOption {

    private String name;
    private float rank;
    private Bitmap image;

    public void setRank(float rank) {
        this.rank = rank;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ItemOption(String name, float rank) {
        this.name = name;
        this.rank = rank;
    }

    public ItemOption(String name, float rank, Bitmap image) {
        this.name = name;
        this.rank = rank;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRank() {
        return rank;
    }

    public static ArrayList<ItemOption> createItemOptions(int numOfItems) {
        ArrayList<ItemOption> createdItems = new ArrayList<>();
        for(int i=0; i<numOfItems; i++) {
            createdItems.add(new ItemOption("Food",(int)Math.random()));
        }
        return createdItems;
    }

}
