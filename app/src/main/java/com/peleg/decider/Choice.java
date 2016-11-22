package com.peleg.decider;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Annie on 11/3/16.
 */
public class Choice {

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

    public Choice(String name, float rank) {
        this.name = name;
        this.rank = rank;
    }

    public Choice(String name, float rank, Bitmap image) {
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

    public static ArrayList<Choice> createItemOptions(int numOfItems) {
        ArrayList<Choice> createdItems = new ArrayList<>();
        for(int i=0; i<numOfItems; i++) {
            createdItems.add(new Choice("Food",(int)Math.random()));
        }
        return createdItems;
    }

}
