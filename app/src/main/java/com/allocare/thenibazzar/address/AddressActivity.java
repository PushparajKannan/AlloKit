package com.allocare.thenibazzar.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allocare.thenibazzar.R;
import com.allocare.thenibazzar.kitmodule.GPS;
import com.allocare.thenibazzar.kitmodule.IGPSActivity;
import com.allocare.thenibazzar.kitmodule.LocationValueModel;
import com.allocare.thenibazzar.kitmodule.Utility;
import com.google.android.material.textfield.TextInputEditText;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddressActivity extends AppCompatActivity implements IGPSActivity {


    CardView currentLocation;
    TextInputEditText streetnumber, city,phonenumberText,idPinceode, state;

    Context mActivity;
    AlertDialog alert;

    private static final int PERMISSION_REQUEST_CODE_MAIN = 300;

    private GPS gps;

    String adrdress="";

    TextView save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        mActivity = this;

        alert = new AlertDialog.Builder(mActivity)
                .setTitle("Conform Your Location")  // GPS not found
                .setMessage("Are you sure to proceed") // Want to enable?
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {


                        FinishComplete(adrdress);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), null).create();

        currentLocation = findViewById(R.id.currentLocation);
        streetnumber = findViewById(R.id.streetnumber);
        city = findViewById(R.id.city);
        phonenumberText = findViewById(R.id.phonenumberText);
        idPinceode = findViewById(R.id.idPinceode);
        state = findViewById(R.id.state);
        save = findViewById(R.id.save);


        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(LocationValueModel.getmLatitude() != 0.0 && LocationValueModel.getmLongitude() != 0.0)
                {

                    adrdress = Utility.getAddress(mActivity,LocationValueModel.getmLatitude(),LocationValueModel.getmLongitude());

                    alert.setMessage(adrdress);

                    alertShow();

                }else {
                    Toast.makeText(mActivity, "Wait Location fetching", Toast.LENGTH_SHORT).show();

                }






            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(streetnumber.getText().toString().trim()) &&
                        !TextUtils.isEmpty(city.getText().toString().trim()) &&
                        !TextUtils.isEmpty(state.getText().toString().trim()) &&
                        !TextUtils.isEmpty(idPinceode.getText().toString().trim()))
                {

                    if(Utility.isValidPin(idPinceode.getText().toString().trim())) {

                        String steert="" , citys="", states="",idpincodes="";

                        steert = streetnumber.getText().toString().trim();
                        citys = city.getText().toString().trim();
                        states = state.getText().toString().trim();
                        idpincodes = idPinceode.getText().toString().trim();



                        adrdress = steert +" "+ citys+" "+states+" "+idpincodes;


                        FinishComplete(adrdress);





                       // finish();
                    }else {

                        Toast.makeText(mActivity, getResources().getString(R.string.invalid_pin), Toast.LENGTH_SHORT).show();

                    }



                }else if(TextUtils.isEmpty(streetnumber.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_stret), Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(city.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_cityname), Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(state.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_cityname), Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(idPinceode.getText().toString().trim()))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.enter_pincode), Toast.LENGTH_SHORT).show();

                }

            }
        });



    }

    private void getAddress()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },5000);
    }


    private void FinishComplete(String text)
    {
        Intent intent=new Intent();
        intent.putExtra("addres",text);
       // setResult(Activity.RESULT_OK,intent);

        setResult(RESULT_OK, intent);

        Toast.makeText(mActivity, getResources().getString(R.string.new_address_saved), Toast.LENGTH_SHORT).show();

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(checkPermissionMain()) {
            if(gps!=null) {
                if (!gps.isRunning()) gps.resumeGPS();
            }
        }

        getLocationWithPermission();

    }

    private void getLocationWithPermission() {

        if(checkPermissionMain()) {
            gps = new GPS(this);
        }else {
            requestPermissionMain();
        }

    }

    public void alertShow()
    {
        if(alert!=null) {
            if(!alert.isShowing()) {
                alert.show();
            }
        }
    }

    public void alertDismiss() {
        if(alert!=null) {
            if(alert.isShowing()) {
                alert.dismiss();
            }
        }
    }

    @Override
    protected void onStop() {
        gps.stopGPS();
        super.onStop();
    }

    public boolean checkPermissionMain() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        //int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED; //&& result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionMain() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE_MAIN);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {


            case PERMISSION_REQUEST_CODE_MAIN:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    // boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {

                        gps = new GPS(this);


                    }
                        /*////Here to do the task
                        lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
                        boolean gps_enabled = false;
                        try {
                            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        } catch (Exception ex) {
                        }
                        if (gps_enabled) {
                            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
                            //if(latitude.equals("") && longitude.equals(""))
                            //{
                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location!=null)
                            {
                                if(location.getLatitude() != 0.0) {
                                    LocationValueModel.setmLatitude(location.getLatitude());
                                }

                                if(location.getLongitude()!= 0.0) {
                                    LocationValueModel.setmLongitude(location.getLongitude());
                                }

                                // longitude = String.valueOf(location.getLongitude());
                                // latitude = String.valueOf(location.getLatitude());
                            }else
                            {
                                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
                            }
                            //  }
                            // doAction();
                        } else {
                            new AlertDialog.Builder(mActivity)
                                    .setTitle(getResources().getString(R.string.LocationisNotEnable))  // GPS not found
                                    .setMessage(getResources().getString(R.string.Wanttoenable)) // Want to enable?
                                    .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.No), null)
                                    .show();
                        }
                    }*/
                }
                break;


        }
    }


    @Override
    public void locationChanged(double longitude, double latitude) {
        Log.e("Location","Long-->"+longitude + "Lat-->"+ latitude);
        if(latitude != 0.0) {
            LocationValueModel.setmLatitude(latitude);
        }

        if(longitude != 0.0) {
            LocationValueModel.setmLongitude(longitude);
        }
    }

    @Override
    public void displayGPSSettingsDialog() {

    }
}
