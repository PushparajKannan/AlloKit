package com.allocare.allokit.kitmodule;

public class LocationValueModel {

    public static double mLatitude;
    public static double mLongitude;


    public static double mDistance;


    public static double getmLatitude() {
        return mLatitude;
    }

    public static void setmLatitude(double mLatitude) {
        LocationValueModel.mLatitude = mLatitude;
    }

    public static double getmLongitude() {
        return mLongitude;
    }

    public static void setmLongitude(double mLongitude) {
        LocationValueModel.mLongitude = mLongitude;
    }

    public static double getmDistance() {
        return mDistance;
    }

    public static void setmDistance(double mDistance) {
        LocationValueModel.mDistance = mDistance;
    }
}
