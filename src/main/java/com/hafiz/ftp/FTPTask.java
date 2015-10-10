package com.hafiz.ftp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
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
    private String[] args;
    private FTPFile[] files;

    public FtpTask(Context mcontext, Map<String, String> msite, String task) {
        context = mcontext;
        site = msite;
    }

    public FtpTask(Context mcontext, Map<String, String> msite, String task, String[] args) {
        context = mcontext;
        site = msite;
    }

    protected FTPClient doInBackground(Void... args) {
        /*
        FTPSClient ftpClient = new FTPSClient("TLS", false);
        try {
            TrustManager[] trustManager = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            ftpClient.setTrustManager(trustManager[0]);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyManager km = kmf.getKeyManagers()[0];
            ftpClient.setKeyManager(km);
            ftpClient.setBufferSize(1024 * 1024);
            ftpClient.setConnectTimeout(100000);

            ftpClient.connect(InetAddress.getByName("server149.web-hosting.com"), 21);
            ftpClient.setSoTimeout(100000);
            if (ftpClient.login("howtjmrk", "zFUBUWLEjWdiQ")) {
                ftpClient.execPBSZ(0);
                ftpClient.execPROT("P");
                ftpClient.changeWorkingDirectory("/");
                Log.d("Reply string", ftpClient.getReplyString());
                if (ftpClient.getReplyString().contains("250")) {
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    ftpClient.enterLocalPassiveMode();
//                    BufferedInputStream buffIn = null;
                }
            }

        } catch (SocketException e) {
            Log.e("APPTAG", e.getStackTrace().toString());
        } catch (UnknownHostException e) {
            Log.e("APPTAG", e.getStackTrace().toString());
        } catch (IOException e) {
            Log.e("APPTAG", e.getStackTrace().toString());
        } catch (Exception e) {
            Log.e("APPTAG", e.getStackTrace().toString());
        } finally {
            try {
                ftpClient.logout();
            } catch (Exception e2) {
            }
            try {
                ftpClient.disconnect();
            } catch (Exception e2) {
            }
        }
*/
        Log.d("siteobj", site.toString());
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(site.get("host"));
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login(site.get("login_name"), site.get("password"));
            //ftpClient.enterRemotePassiveMode();
            //ftpClient.changeWorkingDirectory("/");
            //FTPListParseEngine engine = ftpClient.initiateListParsing()
            switch (task) {
                case "list":
                    files = ftpClient.listFiles("/");
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

            String reply = ftpClient.getReplyString();
            if (ftpClient.getReplyString().contains("250")) {
                message = "Files loaded";
            } else {
                message = reply;
            }

            /*
            BufferedInputStream buffIn = null;

            for (FTPFile file : files) {
                Log.d("filename", file.getName());
            }

            String[] fileNames = ftpClient.listNames();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    Log.d("filename", fileName);
                }
            }
            */

        } catch (Exception e) {
            exception = e.toString();
        }

        return ftpClient;
    }

    protected void onPostExecute(FTPClient result) {
        Log.d("result",files.toString());
        if(exception != null) {
            message = exception;
        }

        switch (task) {
            case "list":
                delegate.processListResponse(message, files);
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


        //Where ftpClient is a instance variable in the main activity
    }

    @Override
    protected void onPreExecute() {
        exception = null;
    }
}
