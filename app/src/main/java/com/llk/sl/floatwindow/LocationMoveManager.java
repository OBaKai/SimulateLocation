package com.llk.sl.floatwindow;

public class LocationMoveManager {
    public static LocationMoveManager getInstance() {
        return LocationMoveManager.InnerHolder.mInstance;
    }

    private static class InnerHolder {
        private static LocationMoveManager mInstance = new LocationMoveManager();
    }

    private double longitude = 0;
    private double latitude = 0;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void clear() {
        longitude = 0;
        latitude = 0;
    }
}
