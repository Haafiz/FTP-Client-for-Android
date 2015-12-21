package com.hafiz.ftp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Created by Hafiz on 9/13/2015.
 */
class FtpTask extends AsyncTask<Void, Void, FTPClient> {

    public FTPResponse delegate=null;

    FTPClient ftpClient;
    private Exception exception;
    private Context context;
    private String message;
    private Map<String, String> site;
    private String task = "list";
    private Bundle args  = null;
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

    public FtpTask(Context mcontext, Map<String, String> msite, String mtask, String path, Bundle args) {
        context = mcontext;
        site = msite;
        task = mtask;
        this.path = path;
        this.args = args;
    }

    protected FTPClient doInBackground(Void... args) {
        Log.d("siteobj", site.toString());

        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(site.get("host"));
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
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
            exception = e;
        }

        return ftpClient;
    }

    private FTPClient performTask (FTPClient ftpClient) {
        if(path == null) {
            path = "/";
        }

        String filename, localPath;
        File localDir;

        try {
            switch (task) {
                case "list":
                    ftpClient.changeWorkingDirectory(path);
                    files = ftpClient.listFiles();
                    workingDirectory = ftpClient.printWorkingDirectory();
                    Log.d("working directory", workingDirectory);
                    break;
                case "upload":
                    ftpClient.changeWorkingDirectory(path);

                    filename = args.getString("filename");
                    localPath = args.getString("localPath");
                    localDir = new File(localPath, filename);
                    //File file = new File(localDir, filename);
                    Log.d("localDir", localDir.getPath());
                    OutputStream outputStream = new FileOutputStream(localDir);
                    Log.d("output stream", outputStream.toString());
                    Boolean success = ftpClient.retrieveFile (filename, outputStream);

                    workingDirectory = ftpClient.printWorkingDirectory();
                    Log.d("working directory", workingDirectory);
                    break;
                case "rename":
                        ftpClient.changeWorkingDirectory(path);
                        Log.d(args.getString("filename"), args.getString("rename"));
                        Boolean rename = ftpClient.rename(args.getString("filename"), args.getString("rename"));

                        files = ftpClient.listFiles();
                        workingDirectory = ftpClient.printWorkingDirectory();
                    break;
                case "download":
                    ftpClient.changeWorkingDirectory(path);

                    filename = args.getString("filename");
                    localPath = args.getString("localPath");
                    File localFile = new File(localPath);

                    //File file = new File(localDir, filename);
                    Log.d("localDir", localFile.getPath());
                    InputStream inputStream = new FileInputStream(localFile);
                    Log.d("output stream", inputStream.toString());
                    Boolean success = ftpClient.storeFile (filename, inputStream);

                    workingDirectory = ftpClient.printWorkingDirectory();
                    Log.d("working directory", workingDirectory);
                    break;
                case "delete":
                    break;
                default:
                    files = ftpClient.listFiles("/");
            }
        } catch (IOException ex){
            ex.printStackTrace();
            //Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
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
            delegate.handleException(exception);
        } else {
            switch (task) {
                case "list":
                    delegate.processListResponse(message, files, workingDirectory);
                    break;
                case "rename":
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
