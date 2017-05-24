package com.ifnill.realmaddressbook;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import io.realm.Realm;

/**
 * Created by bo on 2017. 5. 24..
 */

public class RealmApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Realm.init(mContext);
    }

    public static Context getContext(){
        return mContext;
    }
}
