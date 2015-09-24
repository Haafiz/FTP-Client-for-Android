package com.hafiz.ftp;

/**
 * Created by Hafiz on 9/8/2015.
 */
import android.provider.BaseColumns;

public final class DBContract {

    public DBContract(){}

    public static abstract class Site implements BaseColumns{
        public static final String TABLE_NAME = "site";
        public static final String COLUMN_NAME_SITE_NAME = "site_name";
        public static final String COLUMN_NAME_LOGIN = "login_name";
        public static final String COLUMN_NAME_HOST = "host";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_TYPE = "type";
    }
}