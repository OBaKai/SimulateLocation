package com.llk.sl;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private Handler mH = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (tv != null) {
                tv.setText((String) msg.obj);
            }
        }
    };


    private MockLocationManager mMock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        mMock = new MockLocationManager(this, new MockLocationManager.MockLocationListener() {
            @Override
            public void onLocationChanged(Location mlocal) {
                String strResult = "getAccuracy:" + mlocal.getAccuracy() + "\r\n"
                        + "getAltitude:" + mlocal.getAltitude() + "\r\n"
                        + "getBearing:" + mlocal.getBearing() + "\r\n"
                        + "getElapsedRealtimeNanos:" + String.valueOf(mlocal.getElapsedRealtimeNanos()) + "\r\n"
                        + "getLatitude:" + mlocal.getLatitude() + "\r\n"
                        + "getLongitude:" + mlocal.getLongitude() + "\r\n"
                        + "getProvider:" + mlocal.getProvider() + "\r\n"
                        + "getSpeed:" + mlocal.getSpeed() + "\r\n"
                        + "getTime:" + mlocal.getTime() + "\r\n";
                //Log.i("llk", strResult);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = strResult;
                mH.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMock.stopMockLocation();
    }

    public void simulate(View view) {
        mMock.loopMockLocation();
    }
}
