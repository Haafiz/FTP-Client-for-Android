package com.hafiz.ftp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

    public class RemoteTabFragment extends Fragment implements FTPResponse{

        ArrayAdapter dataAdapter = null;
        Map<String, String> site;
        Bundle args;
        Context context;
        View view;
        EditText addressBar;
        Map<String, FTPFile> fileMap = new HashMap<>();
        RemoteTabFragment taskDelegate = this;

        public void setAddressBarText(String path) {
            addressBar.setText(path);
        }

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

            view = inflater.inflate(R.layout.fragment_layout, container, false);

            addressBar = (EditText) view.findViewById(R.id.address_bar);

            String sitename = args.getString("sitename");
            DatabaseHandler dbHandler = new DatabaseHandler(context);
            site = dbHandler.getSite(sitename);

            FtpTask task = new FtpTask(context, site, "list");
            task.delegate = taskDelegate;
            task.execute();

            return view;
        }

        public void processListResponse(String output, FTPFile[] files, String workingDirectory){
            ArrayList<String> filenamesList = new ArrayList();

            for (FTPFile file : files) {
                fileMap.put(file.getName(), file);
                filenamesList.add(file.getName());
            }

            String[] filenames = new String[filenamesList.size()];
            filenames = filenamesList.toArray(filenames);

            ListCustomAdapter adapter = new ListCustomAdapter(getActivity(), filenames);

            ListView listView =  (ListView) view.findViewById(R.id.listview);
            listView.setAdapter(adapter);

            setAddressBarText(workingDirectory);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("onItemClick", "" + position);
                    // When clicked, show a toast with the TextView text
                    String filename = (String) parent.getItemAtPosition(position);
                    if (fileMap.get(filename).isDirectory()) {
                        String appendablePath = addressBar.getText().toString();
                        if(appendablePath == "/"){
                            appendablePath = "";
                        }

                        FtpTask task = new FtpTask(context, site, "list", (appendablePath +"/"+ filename) );
                        task.delegate = taskDelegate;
                        task.execute();
                    }
                    Toast.makeText(getContext(),
                            "Clicked on Row: " + filename,
                            Toast.LENGTH_LONG).show();
                }
            });
            //ListViewAutoScrollHelper listViewAutoScrollHelper = new ListViewAutoScrollHelper(listView);
        }

    public void processUploadResponse(String output) {

    }

    public void processDownloadResponse(String output) {

    }

    public void processDeleteResponse(String output) {

    }
}