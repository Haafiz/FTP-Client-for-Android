package com.hafiz.ftp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Map;

public class SiteManager extends Activity {

    SQLiteDatabase db;
    DatabaseHandler dbHandler;
    String editId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_manager);

        dbHandler = new DatabaseHandler(getApplicationContext());
        db = dbHandler.getWritableDatabase();

        Intent intent = getIntent();
        if(intent.hasExtra("site")){
            String sitename = intent.getStringExtra("site");
            populateForm(sitename);
        }
    }

    public void populateForm(String sitename) {
        Map<String, String> site = dbHandler.getSite(sitename);
        Log.d("Map", site.toString());
        editId = site.get("_id");

        EditText site_name = (EditText) findViewById(R.id.site_name);
        site_name.setText(site.get("site_name"));

        EditText login = (EditText) findViewById(R.id.login);
        login.setText(site.get("login_name"));

        EditText host = (EditText) findViewById(R.id.host);
        host.setText(site.get("host"));

        EditText password = (EditText) findViewById(R.id.password);
        password.setText(site.get("password"));

        RadioGroup grp =  (RadioGroup) findViewById(R.id.type);
        if(site.get("type") == "Active") {
            RadioButton rb = (RadioButton) findViewById(R.id.active);
            rb.setChecked(true);
        }else {
            RadioButton rb = (RadioButton) findViewById(R.id.passive);
            rb.setChecked(true);
        }
    }

    public boolean saveSite(View view){
        EditText sitename = (EditText) findViewById(R.id.site_name);
        EditText login = (EditText) findViewById(R.id.login);
        EditText host = (EditText) findViewById(R.id.host);
        EditText password = (EditText) findViewById(R.id.password);
        RadioGroup grp =  (RadioGroup) findViewById(R.id.type);

        int selectedID = grp.getCheckedRadioButtonId();
        RadioButton type = (RadioButton) findViewById(selectedID);

        if(sitename.length() < 1) {
            Toast.makeText(getApplicationContext(), "site name cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        } else if(host.length() < 1) {
            Toast.makeText(getApplicationContext(), "Host cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        } else if(login.length() < 1) {
            Toast.makeText(getApplicationContext(), "Login cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        } else if(password.length() < 1) {
            Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        } else if(type == null) {
            Toast.makeText(getApplicationContext(), "Please select a valid type", Toast.LENGTH_LONG).show();
            return false;
        }

        ContentValues data = new ContentValues();

        data.put(DBContract.Site.COLUMN_NAME_SITE_NAME, sitename.getText().toString());
        data.put(DBContract.Site.COLUMN_NAME_LOGIN, login.getText().toString());
        data.put(DBContract.Site.COLUMN_NAME_HOST, host.getText().toString());
        data.put(DBContract.Site.COLUMN_NAME_PASSWORD, password.getText().toString());
        data.put(DBContract.Site.COLUMN_NAME_TYPE, type.getText().toString());

        long saved = -1;

        db = dbHandler.getWritableDatabase();

        if(editId == null) {
            try {
                saved = db.insertOrThrow(DBContract.Site.TABLE_NAME, null, data);
            } catch (SQLiteConstraintException ex) {
                Toast.makeText(getApplicationContext(),"Site name already exist",Toast.LENGTH_LONG).show();
            }
        } else {
            String[] arr = new String[] {String.valueOf(editId)};
            Log.d("ID", arr[0]);
            try{
                saved = db.update(DBContract.Site.TABLE_NAME, data, DBContract.Site._ID + "=?", arr);
            } catch (SQLiteConstraintException ex) {
                Toast.makeText(getApplicationContext(),"Site name already exist",Toast.LENGTH_LONG).show();
            }
        }

        if( saved != -1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("message", "Connection Saved Successfully");
            startActivity(intent);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site_manager, menu);
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
}
