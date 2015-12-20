package com.hafiz.ftp;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class LocalTabFragment extends Fragment {

    EditText addressBar;
    View view;
    String workingDirectory = null;
    Context context = getContext();
    File file;
    ArrayList<String> filenamesList = new ArrayList<String>();

    public void setAddressBarText(String path) {
        Log.d("path", path);
        addressBar.setText(path);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        addressBar = (EditText) view.findViewById(R.id.address_bar);

        if(workingDirectory == null) {
            try {
                Log.d("directory downloads", Environment.DIRECTORY_DOWNLOADS);

                file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            workingDirectory = file.getPath();
        }

        Boolean fExist = file.exists();
        Log.d("local working directory", workingDirectory);
        Log.d("File exist:", fExist.toString());

        try {
            setAddressBarText(workingDirectory);

            if (file.isDirectory()) {

                File[] files = file.listFiles();
                Log.d("isDirectory","yes");
                for (File f : files) {
                    Log.d("in", f.getName());
                    filenamesList.add(f.getName());
                    Toast.makeText(getContext(), f.getName(), Toast.LENGTH_LONG).show();
                }

                setupListViewAdapter(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    public void setupListViewAdapter(View view){
        Toast.makeText(getContext(), "Size"+filenamesList.size(), Toast.LENGTH_LONG).show();
        String[] filenames = new String[filenamesList.size()];
        filenames = filenamesList.toArray(filenames);

        ListCustomAdapter adapter = new ListCustomAdapter(getActivity(), filenames);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}