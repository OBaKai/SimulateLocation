package com.llk.sl;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.llk.sl.util.PreferenceUtil;

/**
 * author:
 * group:
 * createDate:
 * detail:
 */
public class App extends Application {

    private static Context mC;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("llk", "onCreate in Application");
        mC = this;
        MockLocationManager.getInstance().init(this);
        PreferenceUtil.getInstance().init(this);
    }

    public static Context getContext(){
        return mC;
    }
}
