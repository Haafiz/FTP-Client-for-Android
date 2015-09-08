package com.example.hafiz.ftp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ftp";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_SITES_TABLE = "CREATE TABLE " + DBContract.Site.TABLE_NAME + " (" +
                DBContract.Site._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.Site.COLUMN_NAME_SITE_NAME + TEXT_TYPE + COMMA_SEP +
                DBContract.Site.COLUMN_NAME_LOGIN + TEXT_TYPE + COMMA_SEP +
                DBContract.Site.COLUMN_NAME_HOST + TEXT_TYPE + COMMA_SEP +
                DBContract.Site.COLUMN_NAME_PASSWORD + TEXT_TYPE + " );";
        db.execSQL(CREATE_SITES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Site.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new site into sites table
     * */
    public void insertSite(String label){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Site.COLUMN_NAME_SITE_NAME, label);

        // Inserting Row
        db.insert(DBContract.Site.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllSites(){
        List<String> sites = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBContract.Site.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                sites.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning sites
        return sites;
    }
}