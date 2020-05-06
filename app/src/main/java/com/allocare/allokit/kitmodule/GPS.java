package com.allocare.allokit.kitmodule;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;

public class GPS {

    private IGPSActivity main;

    // Helper for GPS-Position
    private LocationListener mlocListener;
    private LocationManager mlocManager;

    boolean gps_enabled = false;
    boolean network_enabled = false;

    private boolean isRunning = false;

    public GPS(IGPSActivity main) {
        this.main = main;

        try {
            // GPS Position
            mlocManager = (LocationManager) ((Activity) this.main).getSystemService(Context.LOCATION_SERVICE);



            mlocListener = new MyLocationListener();

            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);


        /*LocationManager lm = (LocationManager) mCtx
                .getSystemService(Context.LOCATION_SERVICE);*/

            gps_enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location net_loc = null, gps_loc = null, finalLoc = null;

            if (gps_enabled)
                gps_loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                net_loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gps_loc != null && net_loc != null) {

                //smaller the number more accurate result will
                if (gps_loc.getAccuracy() > net_loc.getAccuracy()) {
                    finalLoc = net_loc;

                    if(finalLoc.getLatitude() != 0.0) {
                        LocationValueModel.setmLatitude(finalLoc.getLatitude());
                    }

                    if(finalLoc.getLongitude()!= 0.0) {
                        LocationValueModel.setmLongitude(finalLoc.getLongitude());
                    }
                }
                else {
                    finalLoc = gps_loc;

                    if(finalLoc.getLatitude() != 0.0) {
                        LocationValueModel.setmLatitude(finalLoc.getLatitude());
                    }

                    if(finalLoc.getLongitude()!= 0.0) {
                        LocationValueModel.setmLongitude(finalLoc.getLongitude());
                    }

                }

                // I used this just to get an idea (if both avail, its upto you which you want to take as I've taken location with more accuracy)

            } else {

                if (gps_loc != null) {
                    finalLoc = gps_loc;
                    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

                    if(finalLoc.getLatitude() != 0.0) {
                        LocationValueModel.setmLatitude(finalLoc.getLatitude());
                    }

                    if(finalLoc.getLongitude()!= 0.0) {
                        LocationValueModel.setmLongitude(finalLoc.getLongitude());
                    }

                } else if (net_loc != null) {
                    finalLoc = net_loc;
                    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                    if(finalLoc.getLatitude() != 0.0) {
                        LocationValueModel.setmLatitude(finalLoc.getLatitude());
                    }

                    if(finalLoc.getLongitude()!= 0.0) {
                        LocationValueModel.setmLongitude(finalLoc.getLongitude());
                    }
                }
            }

            // GPS Position END
            this.isRunning = true;
        }catch (SecurityException e)
        {
            e.printStackTrace();
        }

    }

    public void stopGPS() {
        if(isRunning) {
            mlocManager.removeUpdates(mlocListener);
            this.isRunning = false;
        }
    }

    public void resumeGPS() {
        try {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            this.isRunning = true;

            if(mlocManager!=null) {
                mlocManager.addGpsStatusListener(new android.location.GpsStatus.Listener() {
                    public void onGpsStatusChanged(int event) {
                        switch (event) {
                            case GPS_EVENT_STARTED:
                                // do your tasks
                                break;
                            case GPS_EVENT_STOPPED:
                                // do your tasks
                                break;
                        }
                    }
                });
            }

        }catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public class MyLocationListener implements LocationListener {

        private final String TAG = MyLocationListener.class.getSimpleName();

        @Override
        public void onLocationChanged(Location loc) {
            GPS.this.main.locationChanged(loc.getLongitude(), loc.getLatitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
            GPS.this.main.displayGPSSettingsDialog();
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

}
