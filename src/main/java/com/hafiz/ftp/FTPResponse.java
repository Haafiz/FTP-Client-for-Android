package com.hafiz.ftp;

/**
 * Created by Hafiz on 9/25/2015.
 */
public interface FTPResponse {
    void processListResponse(String output);
    void processUploadResponse(String output);
    void processDownloadResponse(String output);
    void processDeleteResponse(String output);
}
