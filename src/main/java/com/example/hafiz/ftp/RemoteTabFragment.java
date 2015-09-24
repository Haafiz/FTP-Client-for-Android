package com.example.hafiz.ftp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RemoteTabFragment extends Fragment {

    ArrayAdapter dataAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Array list of countries
        ArrayList<String> countryList = new ArrayList<String>();
        String country = "Afghanistan";
        countryList.add(country);
        country = "Albania";
        countryList.add(country);
        country = "Algeria";
        countryList.add(country);

        dataAdapter = new ArrayAdapter(getContext(), R.layout.list_row, countryList);

        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        TabActivity activity = (TabActivity) this.getActivity();

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };

        ListCustomAdapter adapter = new ListCustomAdapter(getActivity(), values);

        ListView listView =  (ListView) v.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        ArrayList<String> adapterValues = adapter.getSelected();

        Toast.makeText(getContext(),
                adapterValues.toString(),
                Toast.LENGTH_LONG).show();

        /*
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
}