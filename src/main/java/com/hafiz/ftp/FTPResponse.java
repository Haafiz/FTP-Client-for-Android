package com.hafiz.ftp;

import org.apache.commons.net.ftp.FTPFile;

/**
 * Created by Hafiz on 9/25/2015.
 */
public interface FTPResponse {
    void processListResponse(String output,FTPFile[] files);
    void processUploadResponse(String output);
    void processDownloadResponse(String output);
    void processDeleteResponse(String output);
}
