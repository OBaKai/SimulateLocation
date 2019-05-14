package com.llk.sl;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.llk.sl.eventbus.FloatWindowLocationEvent;
import com.llk.sl.floatwindow.MoveFloatWindowManager;

/**
 * author:
 * group:
 * createDate:
 * detail:
 */
public class MockService extends Service {

//    private NotificationMan man;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("llk", "MockService onCreate");

//        man = new NotificationMan(this, new NotificationMan.NotificationBtnCallBack() {
//            @Override
//            public void onBtnClickAction(String action) {
//                switch (action){
//                    case NotificationMan.ACT_TOP:
//                        break;
//                    case NotificationMan.ACT_BOTTOM:
//                        break;
//                    case NotificationMan.ACT_LEFT:
//                        break;
//                    case NotificationMan.ACT_RIGHT:
//                        break;
//                }
//            }
//        });

        MoveFloatWindowManager.getInstance().init(this);

        startForeground();
    }

    private void startForeground() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MockService.class), 0);

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Foreground Service")
                    .setContentText("Foreground Service Started.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            notification.contentIntent = contentIntent;
            startForeground(1, notification);
        }
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
//        man.destory();

        MoveFloatWindowManager.getInstance().removeFloatWindow();
    }
}
