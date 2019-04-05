package com.johnson.bid;

import android.app.Application;
import android.content.Context;

public class Bid extends Application {

    private static Context mContext;

    public Bid() {}

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }
}
