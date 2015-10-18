package com.hafiz.ftp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Created by Hafiz on 9/13/2015.
 */
class FtpTask extends AsyncTask<Void, Void, FTPClient> {

    public FTPResponse delegate=null;

    FTPClient ftpClient;
    private String exception;
    private Context context;
    private String message;
    private Map<String, String> site;
    private String task = "list";
    private String[] args  = null;
    private FTPFile[] files;
    String path = null;
    String workingDirectory;

    public FtpTask(Context mcontext, Map<String, String> msite, String mtask) {
        context = mcontext;
        site = msite;
        task = mtask;
    }

    public FtpTask(Context mcontext, Map<String, String> msite, String mtask, String path) {
        context = mcontext;
        site = msite;
        task = mtask;
        this.path = path;
    }

    protected FTPClient doInBackground(Void... args) {
        Log.d("siteobj", site.toString());

        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(site.get("host"));
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login(site.get("login_name"), site.get("password"));

            performTask(ftpClient);

            String reply = ftpClient.getReplyString();
            if (ftpClient.getReplyString().contains("250")) {
                message = "Files loaded";
            } else {
                message = reply;
            }


        } catch (Exception e) {
            exception = e.toString();
        }

        return ftpClient;
    }

    private FTPClient performTask (FTPClient ftpClient) {
        if(path == null) {
            path = "/";
        }
        try {
            switch (task) {
                case "list":
                    ftpClient.changeWorkingDirectory(path);
                    files = ftpClient.listFiles();
                    workingDirectory = ftpClient.printWorkingDirectory();
                    Log.d("working directory", workingDirectory);
                    break;
                case "upload":
                    break;
                case "download":
                    break;
                case "delete":
                    break;
                default:
                    files = ftpClient.listFiles("/");
            }
        } catch (IOException ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }

        return ftpClient;
    }

    /**
     * Post execute event handler, give control to appropriate delegate method
     *
     * @param result
     */
    protected void onPostExecute(FTPClient result) {

        if(exception != null) {
            message = exception;
        } else {
            switch (task) {
                case "list":
                    delegate.processListResponse(message, files, workingDirectory);
                    break;
                case "upload":
                    delegate.processUploadResponse(message);
                    break;
                case "download":
                    delegate.processDownloadResponse(message);
                    break;
                case "delete":
                    delegate.processDeleteResponse(message);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPreExecute() {
        exception = null;
    }
}
