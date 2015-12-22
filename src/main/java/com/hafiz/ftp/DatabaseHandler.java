package com.hafiz.ftp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                DBContract.Site.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                DBContract.Site.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP + " UNIQUE("+DBContract.Site.COLUMN_NAME_SITE_NAME+") );";
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
     * Getting all sites
     * returns list of sites
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

    public boolean deleteSiteBySitename(String site_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBContract.Site.TABLE_NAME, DBContract.Site.COLUMN_NAME_SITE_NAME + " = ?",
                new String[] { site_name });

        db.close();

        return true;
    }

    public Map<String, String> getSite(String sitename){
        Map<String, String> site = new HashMap<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBContract.Site.TABLE_NAME + " where site_name ='"+sitename+"' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all columns
        if (cursor.moveToFirst()) {
            for( int  i = 0; i < cursor.getColumnCount(); i++){
                site.put(cursor.getColumnName(i), cursor.getString(i));
            }
        }

        // closing connection
        cursor.close();

        // returning sites
        return site;
    }
}