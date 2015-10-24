package com.example.mysidebar;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import android.database.sqlite.SQLiteDatabase;

public class MyApplication extends LitePalApplication {

    public void onCreate() {

        SQLiteDatabase db = Connector.getDatabase();
    }

}
