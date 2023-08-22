package com.store.lamore.data;

import org.osmdroid.util.GeoPoint;

public class OutletLocation {
    private String name;
    private double lat, longi;
    public OutletLocation(){}

    public void setName(String name){
        this.name = name;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public void setLat(String lat){
        this.lat = Double.parseDouble(lat);
    }

    public void setLongi(double longi){
        this.longi = longi;
    }

    public void setLongi(String longi){
        this.longi = Double.parseDouble(longi);
    }

    public void setLocation(GeoPoint point){
        this.longi = point.getLongitude();
        this.lat = point.getLatitude();
    }

    public double getLat(){
        return lat;
    }

    public double getLongi(){
        return longi;
    }

    public GeoPoint getGeoPoint(){
        return (new GeoPoint(lat, longi));
    }

    public String getName(){
        return name;
    }
}
