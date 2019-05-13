package com.llk.sl.db;

public class CollectModel {
    private int id;
    private String note;
    private String address;
    private double longitude;
    private double latitude;

    public CollectModel(int id, String note, String address, double longitude, double latitude) {
        this.id = id;
        this.note = note;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
