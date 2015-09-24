package com.hafiz.ftp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Created by Hafiz on 9/13/2015.
 */
class FtpTask extends AsyncTask<Void, Void, FTPClient> {

    FTPClient ftpClient;
    private String exception;
    private Context context;
    private String message;

    public FtpTask(Context mcontext) {
        context = mcontext;
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
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host);
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login(login, password);
            //ftpClient.enterRemotePassiveMode();
            //ftpClient.changeWorkingDirectory("/");
            //FTPListParseEngine engine = ftpClient.initiateListParsing()
            FTPFile[] files = ftpClient.listFiles("/public_html");
            Log.d("file length", "" + files.length);
            String reply2 = ftpClient.getStatus();
            Log.d("Reply", reply2);
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


        } catch (Exception e) {
            exception = e.toString();
        }
        return ftpClient;
    }

    protected void onPostExecute(FTPClient result) {
        if(exception != null) {
            Toast.makeText(context, exception, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        //Where ftpClient is a instance variable in the main activity
    }

    @Override
    protected void onPreExecute() {
        exception = null;
    }
}
