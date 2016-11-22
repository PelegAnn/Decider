package com.peleg.decider.com.peleg.decider.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.peleg.decider.Choice;

import static com.peleg.decider.com.peleg.decider.db.DbBitmapUtility.getBytes;

/**
 * Created by Annie on 11/14/16.
 */
public class ItemsDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ItemsReader.db";

    public ItemsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemsReaderContract.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ItemsReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long addToDB(Choice item) {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_NAME, item.getName());
        values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_RANK, item.getRank());
        values.put(ItemsReaderContract.ItemEntry.COLUMN_NAME_IMAGE, getBytes(item.getImage()));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ItemsReaderContract.ItemEntry.TABLE_NAME, null, values);
        Log.e("TAG",String.valueOf(newRowId));
        return newRowId;
    }

    public Cursor getItems() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ItemsReaderContract.ItemEntry._ID,
                ItemsReaderContract.ItemEntry.COLUMN_NAME_NAME,
                ItemsReaderContract.ItemEntry.COLUMN_NAME_RANK,
                ItemsReaderContract.ItemEntry.COLUMN_NAME_IMAGE
        };

        String sortOrder =
                ItemsReaderContract.ItemEntry.COLUMN_NAME_NAME + " DESC";
        return db.query(
                ItemsReaderContract.ItemEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }
}
