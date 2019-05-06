package com.llk.sl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


public class MockLocationManager {

    private LocationManager mLocationManager;
    private MockLocationListener mMockLocationListener;
    private Context mContext;

    private double mLat, mLng;

    private boolean isStopMockLocation = false;

    private boolean isInitOk = false;

    private final static String GPD_PROVIDER_STR = LocationManager.GPS_PROVIDER;

    public MockLocationManager(Context context, MockLocationListener listener) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mMockLocationListener = listener;
        start();
    }

    private void start() {
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.w("llk", "GPS_PROVIDER disable!");
            toast("GPS_PROVIDER disable!");
            isInitOk = false;
            return;
        }

        try {
            LocationProvider provider = mLocationManager.getProvider(GPD_PROVIDER_STR);
            if (provider != null) {
                mLocationManager.addTestProvider(
                        provider.getName()
                        , provider.requiresNetwork()
                        , provider.requiresSatellite()
                        , provider.requiresCell()
                        , provider.hasMonetaryCost()
                        , provider.supportsAltitude()
                        , provider.supportsSpeed()
                        , provider.supportsBearing()
                        , provider.getPowerRequirement()
                        , provider.getAccuracy());
                mLocationManager.setTestProviderEnabled(GPD_PROVIDER_STR, true);
                mLocationManager.setTestProviderStatus(GPD_PROVIDER_STR, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
            }

            if (mLocationListener != null){
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    toast("未获得定位权限");
                    return;
                }

                mLocationManager.requestLocationUpdates(GPD_PROVIDER_STR, 0, 0, mLocationListener);
            }

            isStopMockLocation = false;

            isInitOk = true;
        } catch (Exception e) {
            Log.e("llk", "initLocation: " + e.toString());
            isInitOk = false;
        }
    }

    public void pause() {
        isStopMockLocation = true;
    }

    public void resume(){
        isStopMockLocation = false;
    }

    public boolean isLoop(){
        return isStopMockLocation;
    }

    public void destory(){
        isInitOk = false;
        if (mLocationManager != null) {
            try {
                mLocationManager.removeTestProvider(GPD_PROVIDER_STR);

                if (mLocationListener != null) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListener);
                }
            } catch (Exception e) {
                Log.e("llk", "stopMockLocation: " + e.toString());
            }
        }
    }

    public void setLatLng(double lat, double lng){
        toast("更新 lat=" + lat + " lng=" + lng);
        Log.i("llk", "转换前: lat=" + lat + " lng=" + lng);
        GpsModel model = PositionUtil.bd09_To_Gps84(lat, lng);
        mLat = model.getWgLat();
        mLng = model.getWgLon();
        Log.i("llk", "转换后: lat=" + mLat + " lng=" + mLng);
    }

    public void loopMockLocation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStopMockLocation) {
                    Location mlocation = new Location(GPD_PROVIDER_STR);
                    mlocation.setLongitude(mLng);
                    mlocation.setLatitude(mLat);
                    mlocation.setAltitude(2.0); //海拔
                    mlocation.setAccuracy(3.0f);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            mlocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        }
                        mLocationManager.setTestProviderLocation(GPD_PROVIDER_STR, mlocation);

                        Thread.sleep(500);
                    } catch (Exception e) {
                        return;
                    }
                }

            }
        }).start();
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location mlocal) {
            if (mMockLocationListener != null){
                mMockLocationListener.onLocationChanged(mlocal);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void toast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public interface MockLocationListener{
        void onLocationChanged(Location mlocal);
    }
}
