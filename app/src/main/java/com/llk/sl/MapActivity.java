package com.llk.sl;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

/**
 * author:
 * group:
 * createDate:
 * detail:
 */
public class MapActivity extends Activity implements TencentMap.OnMapClickListener {

    private MockLocationManager mockLocationManager = MockLocationManager.getInstance();

    private LatLng mSelectLatlng;

    private MapView mapView;
    private TencentMap tencentMap;

    private TextView tv;

    private Handler mH = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            tv.setText((String) msg.obj);
        }
    };

    private void requestPermission() {
        PermissionUtil.checkAndRequestPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        }, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }

        mapView = findViewById(R.id.mapview);
        tv = findViewById(R.id.tv);

        tencentMap = mapView.getMap();
        //中心湖
        tencentMap.setCenter(new LatLng(23.04833, 113.399242));
        tencentMap.setZoom(15);
        tencentMap.setOnMapClickListener(this);
        UiSettings uiSettings = mapView.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true);

        mockLocationManager.setMockLocationListener(new MockLocationManager.MockLocationListener() {
            @Override
            public void onLocationChanged(Location mlocal) {
                String strResult =
                        "latitude:" + mlocal.getLatitude() + "\r\n"
                                + "longitude:" + mlocal.getLongitude() + "\r\n"
                                + "elapsedRealtimeNanos:" + mlocal.getElapsedRealtimeNanos() + "\r\n"
                                + "time:" + mlocal.getTime() + "\r\n";
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

        stopService(new Intent(this, MockService.class));

        mockLocationManager.destory();
    }

    public void startMock(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mockLocationManager.toast("我要定位权限，快去设置开下");
            return;
        }

        if (mSelectLatlng == null){
            mockLocationManager.toast("你还没选好位置啊，小老弟");
            return;
        }

        mockLocationManager.resume();
        mockLocationManager.loopMockLocation();

        findViewById(R.id.btn_start).setEnabled(false);
        findViewById(R.id.btn_stop).setEnabled(true);
        mockLocationManager.toast("开始干活");
    }

    public void stopMock(View view) {
        mockLocationManager.pause();

        findViewById(R.id.btn_start).setEnabled(true);
        findViewById(R.id.btn_stop).setEnabled(false);

        tv.setText("你好啊小老弟");
        mockLocationManager.toast("停止干活");
    }

//    public void more(View view) {
//
//    }

    @Override
    public void onMapClick(LatLng latLng) {
        mSelectLatlng = latLng;

        tencentMap.clearAllOverlays();

        Marker marker = tencentMap.addMarker(new MarkerOptions()
                .position(mSelectLatlng)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker()));
        marker.showInfoWindow();// 设置默认显示一个infoWindow

        mockLocationManager.setLatLng(mSelectLatlng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("llk", "permission ok!");
    }
}
