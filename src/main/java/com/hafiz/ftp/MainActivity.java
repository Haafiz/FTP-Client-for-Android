package com.hafiz.ftp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * First screen to showing different option, before connecting to FTP server
 */
public class MainActivity extends Activity implements OnItemSelectedListener {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File f = Environment.getExternalStoragePublicDirectory("");

        // database handler
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        //addDefaultSite(dbHandler);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        loadSpinnerData(dbHandler);

        this.showIntentMessage();

    }

    /**
     * Show and return if there is message String in intent extra
     *
     * @return String message
     */
    private String showIntentMessage () {
        Intent intent = getIntent();
        String message = null;

        if(intent.hasExtra("message")){
            message = intent.getStringExtra("message");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        return message;
    }

    /**
     * Just to call once to set up default site for testing purpose
     */
    public void addDefaultSite(DatabaseHandler dbHandler) {

        //DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues data = new ContentValues();

        data.put(DBContract.Site.COLUMN_NAME_SITE_NAME, "default");
        data.put(DBContract.Site.COLUMN_NAME_LOGIN, "howtjmrk");
        data.put(DBContract.Site.COLUMN_NAME_HOST, "server149.web-hosting.com");
        data.put(DBContract.Site.COLUMN_NAME_PASSWORD, "zFUBUWLEjWdiQ");
        data.put(DBContract.Site.COLUMN_NAME_TYPE, "passive");

        try {
            db.insertWithOnConflict(DBContract.Site.TABLE_NAME, null, data, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (Exception ex) {

        }

    }

    /**
     * Event handle for Add new site button
     *
     * @param view
     */
    public void newSite(View view) {
        Intent intent = new Intent(this, SiteManager.class);
        startActivity(intent);
    }

    /**
     * Connecting to selected FTP site, it is event handler for connect button click
     *
     * @param view
     */
    public void connect(View view) {
        Intent intent = new Intent(this, TabActivity.class);
        Object item = spinner.getSelectedItem();

        intent.putExtra("site", item.toString());
        startActivity(intent);
    }

    /**
     * Edit button click event handler
     *
     * @param view
     */
    public void editSite(View view) {
        Intent intent = new Intent(this, SiteManager.class);
        Object item = spinner.getSelectedItem();

        intent.putExtra("site", item.toString());
        startActivity(intent);
    }

    /**
     * Event handler for delete button click, it deletes selected site
     *
     * @param view
     */
    public void deleteSite(View view) {
        Object item = spinner.getSelectedItem();

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        boolean deleted = false;

        try{
            deleted = dbHandler.deleteSiteBySitename(item.toString());
        } catch (SQLiteException ex) {
            Toast.makeText(getApplicationContext(), ex.toString(),Toast.LENGTH_LONG).show();
        }

        if(deleted) {
            loadSpinnerData(dbHandler);
            Toast.makeText(getApplicationContext(), "Site '"+item.toString()+"' Deleted Successfully",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method loads data of sites in spinner from database
     *
     * @param dbHandler
     */
    private void loadSpinnerData(DatabaseHandler dbHandler) {
        // Spinner Drop down elements
        List<String> labels = dbHandler.getAllSites();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if(labels.size() == 0){
            Button deleteButton = (Button) findViewById(R.id.delete_button);
            Button editButton = (Button) findViewById(R.id.edit_button);
            Button connectButton = (Button) findViewById(R.id.connect_button);

            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
            connectButton.setEnabled(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
