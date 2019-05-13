package com.llk.sl.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.llk.sl.MockLocationManager;
import com.llk.sl.MockService;
import com.llk.sl.PermissionUtil;
import com.llk.sl.R;
import com.llk.sl.db.CollectDao;
import com.llk.sl.eventbus.CollectEvent;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author:
 * group:
 * createDate:
 * detail:
 */
public class MapActivity extends AppCompatActivity implements TencentMap.OnMapClickListener {

    private MockLocationManager mockLocationManager = MockLocationManager.getInstance();

    private LatLng mSelectLatlng;

    private Toolbar toolbar;

    private View infoWindowView;
    private TextView longitude;
    private TextView latitude;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }

        setContentView(R.layout.act_main);

        EventBus.getDefault().register(this);

        Intent sI = new Intent(this, MockService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(sI);
        }else {
            startService(sI);
        }

        initToolbar();

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

        // 初始化数据库
        CollectDao.getInstance().initDB(this);
        initInfoView();

//        CollectDao.getInstance().insert("百果园", "", 22.927907451526092, 113.35694742790618);
//        CollectDao.getInstance().insert("奥园广场", "", 22.92640076089131, 113.35368990524276);
//        CollectDao.getInstance().insert("西丽桥", "", 22.930119962910467, 113.3517465971646);
//        CollectDao.getInstance().insert("花样年华", "", 22.928006441026284, 113.34649496932053);
//        CollectDao.getInstance().insert("仲元", "", 22.933444318409506, 113.35464202480209);
//        CollectDao.getInstance().insert("木偶娃娃", "", 23.059892204174144, 113.40226131327726);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:

                        break;
                    case R.id.action_collect:
                        Intent intent = new Intent(MapActivity.this, CollectActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_open_window:

                        break;
                    case R.id.action_close_window:

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void initInfoView() {
        infoWindowView = LayoutInflater.from(this).inflate(R.layout.view_infowindow, null, false);
        longitude = infoWindowView.findViewById(R.id.tv_longitude);
        latitude = infoWindowView.findViewById(R.id.tv_latitude);

        infoWindowView.findViewById(R.id.btn_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollect();
            }
        });

        infoWindowView.findViewById(R.id.btn_through).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMock(null);
            }
        });
    }

    private void addCollect() {
        View collectView = LayoutInflater.from(this).inflate(R.layout.dialog_collect2, null);
        final EditText noteEt = collectView.findViewById(R.id.modifyedittext);
        TextView textView = collectView.findViewById(R.id.beizhu_textv);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        textView.setText("备注名：");
        builder.setTitle("添加收藏");
        builder.setView(collectView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(noteEt.getText().toString().trim())) {
                    Toast.makeText(MapActivity.this, "请输入备注名", Toast.LENGTH_LONG).show();
                } else {
                    CollectDao.getInstance().insert(noteEt.getText().toString().trim(), "", mSelectLatlng.getLatitude(), mSelectLatlng.getLongitude());
                    Toast.makeText(MapActivity.this, "收藏成功", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MockService.class));

        EventBus.getDefault().unregister(this);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectEvent event) {
        Log.i("MapActivity", "onMessageEvent!!!");
        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
        performMapClick(latLng);
    }

    private void performMapClick(LatLng latLng) {
        mSelectLatlng = latLng;

        tencentMap.clearAllOverlays();
        tencentMap.setCenter(new LatLng(latLng.getLatitude(), latLng.getLongitude()));

        Marker marker = tencentMap.addMarker(new MarkerOptions()
                .position(mSelectLatlng)
                .anchor(0.5f, 1.0f)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .markerView(infoWindowView));
        marker.showInfoWindow();// 设置默认显示一个infoWindow

        longitude.setText("longitude=" + latLng.getLongitude());
        latitude.setText("latitude=" + latLng.getLatitude());

        mockLocationManager.setLatLng(mSelectLatlng);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        performMapClick(latLng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("llk", "permission ok!");
    }
}
