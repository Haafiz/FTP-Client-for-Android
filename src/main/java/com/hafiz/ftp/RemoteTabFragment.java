package com.hafiz.ftp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class RemoteTabFragment extends Fragment implements FTPResponse{

    ArrayAdapter dataAdapter = null;
    Map<String, String> site;
    Bundle args;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    public void setSite(Map<String, String> msite) {
        site = msite;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();
        //Array list of countries
        ArrayList<String> countryList = new ArrayList<String>();
        String country = "Afghanistan";
        countryList.add(country);
        country = "Albania";
        countryList.add(country);
        country = "Algeria";
        countryList.add(country);

        dataAdapter = new ArrayAdapter(context, R.layout.list_row, countryList);

        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        TabActivity activity = (TabActivity) this.getActivity();

        String sitename = args.getString("sitename");
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        Map<String, String> site = dbHandler.getSite(sitename);

        FtpTask task = new FtpTask(context, site, "list");
        task.delegate = this;
        task.execute();

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };

        ListCustomAdapter adapter = new ListCustomAdapter(getActivity(), values);

        ListView listView =  (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(adapter);


        //)
        /*
        ArrayList<String> adapterValues = adapter.getSelected();

        Toast.makeText(getContext(),
                adapterValues.toString(),
                Toast.LENGTH_LONG).show();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                String country = (String) parent.getItemAtPosition(position);
                Toast.makeText(getContext(),
                        "Clicked on Row: " + country,
                        Toast.LENGTH_LONG).show();
            }
        });
        */

        ListViewAutoScrollHelper listViewAutoScrollHelper = new ListViewAutoScrollHelper(listView);

        return v;
    }

    public void processListResponse(String output){

    }

    public void processUploadResponse(String output) {

    }

    public void processDownloadResponse(String output) {

    }

    public void processDeleteResponse(String output) {

    }
}