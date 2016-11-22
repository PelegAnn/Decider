package com.peleg.decider.com.peleg.decider.db;

import android.provider.BaseColumns;

/**
 * Created by Annie on 11/14/16.
 */
public class ItemsReaderContract {

    private ItemsReaderContract() {}

    /* Inner class that defines the table contents */
    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_NAME = "title";
        public static final String COLUMN_NAME_RANK = "rank";
        public static final String COLUMN_NAME_IMAGE = "image";
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String INT_TYPE = " INT";
    public static final String BLOB_TYPE = " BLOB";
    public static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            ItemEntry.TABLE_NAME + "(" + ItemEntry._ID + " INTEGER PRIMARY KEY, " +
            ItemEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            ItemEntry.COLUMN_NAME_RANK + INT_TYPE + COMMA_SEP +
            ItemEntry.COLUMN_NAME_IMAGE  + BLOB_TYPE + ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;

}
