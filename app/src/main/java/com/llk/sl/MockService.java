package com.llk.sl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * author:
 * group:
 * createDate:
 * detail:
 */
public class MockService extends Service {

    private NotificationMan man;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("llk", "MockService onCreate");

        man = new NotificationMan(this, new NotificationMan.NotificationBtnCallBack() {
            @Override
            public void onBtnClickAction(String action) {
                switch (action){
                    case NotificationMan.ACT_TOP:
                        break;
                    case NotificationMan.ACT_BOTTOM:
                        break;
                    case NotificationMan.ACT_LEFT:
                        break;
                    case NotificationMan.ACT_RIGHT:
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("llk", "MockService onDestroy");
        man.destory();
    }
}
