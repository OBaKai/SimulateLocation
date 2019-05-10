package com.llk.sl;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

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

        Intent sI = new Intent(mC, MockService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(sI);
        }else {
            startService(sI);
        }

        MockLocationManager.getInstance().init(this);
    }

    public static Context getContext(){
        return mC;
    }
}
