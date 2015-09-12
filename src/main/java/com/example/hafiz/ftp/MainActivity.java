package com.example.hafiz.ftp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity implements OnItemSelectedListener {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // database handler
        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

        spinner.setOnItemSelectedListener(this);

        loadSpinnerData(dbHandler);

        Intent intent = getIntent();
        if(intent.hasExtra("message")){
            String message = intent.getStringExtra("message");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    public void newSite(View view) {
        Intent intent = new Intent(this, SiteManager.class);
        startActivity(intent);
    }

    public void editSite(View view) {
        Intent intent = new Intent(this, SiteManager.class);
        Object item = spinner.getSelectedItem();

        intent.putExtra("site", item.toString());
        startActivity(intent);
    }

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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

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
}
