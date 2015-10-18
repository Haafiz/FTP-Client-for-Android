package com.hafiz.ftp;

import org.apache.commons.net.ftp.FTPFile;

/**
 * Created by Hafiz on 9/25/2015.
 *
 * Interface to use in on onPostExecute() of Async task of FTPTask
 */
public interface FTPResponse {
    void processListResponse(String output,FTPFile[] files, String workingDirectory);
    void processUploadResponse(String output);
    void processDownloadResponse(String output);
    void processDeleteResponse(String output);
    void setAddressBarText(String path);
}
