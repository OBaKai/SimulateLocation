package com.llk.sl;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private EditText e1, e2;
    private LinearLayout ll;
    private double lat, lng;

    private Map<String, double[]> map = new HashMap<String, double[]>(){{
        put("新天地抓鬼", new double[] {23.0643532636, 113.3869099617});
        put("新天地Coco熊1", new double[] {23.0632180673,113.3891415596});
        put("新天地Coco熊2", new double[] {23.0632970378,113.3868777752});
    }};

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
        ll = findViewById(R.id.layout);
        for (String name : map.keySet()){
            Button button = new Button(this);
            button.setText(name);
            button.setTag(name);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double[] latArray = map.get(v.getTag());

                    mMock.setLatLng(latArray[0], latArray[1]);
                }
            });
            ll.addView(button);
        }

        tv = findViewById(R.id.tv);
        e1 = findViewById(R.id.et1);
        e2 = findViewById(R.id.et2);
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lat = Double.parseDouble(String.valueOf(s));
            }
        });

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lng = Double.parseDouble(String.valueOf(s));
            }
        });

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

    public void updateLatLng(View view) {
        mMock.setLatLng(lat, lng);
    }
}
