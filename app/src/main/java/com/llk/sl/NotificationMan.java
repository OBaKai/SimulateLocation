package com.llk.sl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * author:
 * group:
 * createDate:
 * detail:
 */
public class NotificationMan {

    private Context mC;
    private NotificationManager mN;

    public static final String ACT_TOP = "com.llk.sl.ACT_TOP";
    public static final String ACT_BOTTOM = "com.llk.sl.ACT_BOTTOM";
    public static final String ACT_LEFT = "com.llk.sl.ACT_LEFT";
    public static final String ACT_RIGHT = "com.llk.sl.ACT_RIGHT";

    private static final String CHANNEL_ID = "sl_666";
    private static final int NOTIFY_NUM = 9527;

    private NotificationBtnCallBack mCallBack;

    public NotificationMan(Context context, NotificationBtnCallBack callBack){
        mC = context;
        mN = (NotificationManager) mC.getSystemService(NOTIFICATION_SERVICE);
        mCallBack = callBack;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACT_TOP);
        intentFilter.addAction(ACT_BOTTOM);
        intentFilter.addAction(ACT_LEFT);
        intentFilter.addAction(ACT_RIGHT);
        mC.registerReceiver(mBC, intentFilter);

        RemoteViews views = new RemoteViews(mC.getPackageName(), R.layout.notify_layout);
        views.setOnClickPendingIntent(R.id.btn_top, getPendingIntent(ACT_TOP));
        views.setOnClickPendingIntent(R.id.btn_bottom, getPendingIntent(ACT_BOTTOM));
        views.setOnClickPendingIntent(R.id.btn_left, getPendingIntent(ACT_LEFT));
        views.setOnClickPendingIntent(R.id.btn_right, getPendingIntent(ACT_RIGHT));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mC, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContent(views);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "过桥米线", NotificationManager
                    .IMPORTANCE_DEFAULT);
            mN.createNotificationChannel(channel);
        }
        mN.notify(NOTIFY_NUM, builder.build());
    }

    public void destory(){
        mCallBack = null;
        mC.unregisterReceiver(mBC);
        mN.cancel(NOTIFY_NUM);
    }

    private BroadcastReceiver mBC = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;

            String act = intent.getAction();
            Log.e("llk", "onReceive: action=" + act);
            if (!TextUtils.isEmpty(act)
                && mCallBack != null){
                mCallBack.onBtnClickAction(act);
            }


        }
    };

    private PendingIntent getPendingIntent(String action){
        Intent intent = new Intent();
        intent.setAction(action);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mC,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public interface NotificationBtnCallBack{
        void onBtnClickAction(String action);
    }
}
