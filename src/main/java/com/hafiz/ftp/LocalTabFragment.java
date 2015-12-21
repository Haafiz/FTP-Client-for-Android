package com.hafiz.ftp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalTabFragment extends Fragment implements FTPResponse {

    EditText addressBar;
    View view;
    String workingDirectory = null;
    Context context = getContext();
    Bundle args;
    File file;
    //contains site/connection record
    Map<String, String> site;

    //to delegate work to this activity after async task
    LocalTabFragment taskDelegate = this;

    // fileMap and filenamesList will contain loaded files info.
    Map<String, File> fileMap = new HashMap<>();
    ArrayList<String> filenamesList = new ArrayList();

    public void setAddressBarText(String path) {
        Log.d("path", path);
        addressBar.setText(path);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();

        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        addressBar = (EditText) view.findViewById(R.id.address_bar);

        String sitename = args.getString("sitename");
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        site = dbHandler.getSite(sitename);

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
                filenamesList.clear();
                for (File f : files) {
                    filenamesList.add(f.getName());
                    fileMap.put(f.getName(), f);
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



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick", "" + position);
                // When clicked, show a toast with the TextView text
                String filename = (String) parent.getItemAtPosition(position);
                Log.d(filename, filename);

                if (fileMap.get(filename).isDirectory()) {

                } else {
                    Toast.makeText(getContext(), "Uploading: " + filename, Toast.LENGTH_LONG).show();

                    Bundle args = new Bundle();
                    args.putString("filename", filename);
                    args.putString("localPath", fileMap.get(filename).getPath());

                    Log.d("bundle", args.toString());
                    Log.d("address", getRemoteAddress());

                    startFtpTask("upload", getRemoteAddress(), args);
                }
            }
        });
    }

    public String getRemoteAddress() {
        TabActivity parentActivity = (TabActivity) getActivity();
        String remoteAddress = parentActivity.remoteAddress;
//        Log.d("remotetabview", remoteTabView.toString());
//        EditText remoteAddressBar = (EditText) remoteTabView.findViewById(R.id.address_bar);

        return remoteAddress;
    }

    public void startFtpTask(String operation, String directoryPath, Bundle arguments) {
        FtpTask task = new FtpTask(context, site, operation, directoryPath, arguments);
        task.delegate = taskDelegate;
        task.execute();
    }

    public void processUploadResponse(String output) {
        Toast.makeText(getContext(), "File uploaded successfully" , Toast.LENGTH_LONG).show();
    }

    public void processDownloadResponse(String output) {

    }

    public void processDeleteResponse(String output) {

    }

    public void handleException(Exception exception) {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        intent.putExtra("message", exception.toString());
        startActivity(intent);
    }

    public void processListResponse(String output, FTPFile[] files, String workingDirectory) {

    }

}